package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.database.model.Category;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.model.maps.Song;
import de.ixsen.accsaber.database.model.players.ScoreData;
import de.ixsen.accsaber.database.repositories.model.BeatMapRepository;
import de.ixsen.accsaber.database.repositories.model.CategoryRepository;
import de.ixsen.accsaber.database.repositories.model.ScoreDataRepository;
import de.ixsen.accsaber.integration.connector.BeatSaverConnector;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverMapDifficulty;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class RankedMapService {

    private final BeatSaverConnector beatSaverConnector;
    private final BeatMapRepository beatMapRepository;
    private final ScoreDataRepository scoreDataRepository;

    private final SongService songService;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RankedMapService(BeatMapRepository beatMapRepository,
                            ScoreDataRepository scoreDataRepository,
                            CategoryRepository categoryRepository,
                            BeatSaverConnector beatSaverConnector,
                            SongService songService) {
        this.beatMapRepository = beatMapRepository;
        this.scoreDataRepository = scoreDataRepository;
        this.categoryRepository = categoryRepository;
        this.beatSaverConnector = beatSaverConnector;
        this.songService = songService;
    }

    public List<BeatMap> getRankedMaps() {
        return this.beatMapRepository.findAll();
    }

    public BeatMap getRankedMap(Long leaderboardId) {
        Optional<BeatMap> optionalRankedMap = this.beatMapRepository.findById(leaderboardId);
        if (optionalRankedMap.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, "The ranked map with the id could not be found.");
        }
        return optionalRankedMap.get();
    }

    public void addNewRankedMapByKey(String mapKey, String difficulty, Double complexity, String categoryName) {
        BeatSaverSongInfo beatSaverSongInfo = this.beatSaverConnector.getMapInfoByKey(mapKey);
        String songHash = beatSaverSongInfo.getVersions().stream().findFirst().orElseThrow().getHash();
        this.addNewRankedMap(beatSaverSongInfo, songHash, difficulty, complexity, categoryName);
    }

    public void addNewRankedMapByHash(String songHash, String difficulty, Double complexity, String categoryName) {
        BeatSaverSongInfo beatSaverSongInfo = this.beatSaverConnector.getMapInfoByHash(songHash);
        this.addNewRankedMap(beatSaverSongInfo, songHash, difficulty, complexity, categoryName);
    }

    private void addNewRankedMap(BeatSaverSongInfo beatSaverSongInfo, String songHash, String difficulty, Double complexity, String categoryName) {
        Optional<Category> optionalCategory = this.categoryRepository.findByCategoryName(categoryName);
        if (optionalCategory.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.CATEGORY_NOT_FOUND, String.format("The category [%s] was not found.", categoryName));
        }
        Category category = optionalCategory.get();
        long leaderBoardId = this.beatSaverConnector.getScoreSaberId(songHash, this.mapDiffToId(difficulty));
        if (this.beatMapRepository.existsById(leaderBoardId)) {
            throw new AccsaberOperationException(ExceptionType.RANKED_MAP_ALREADY_EXISTS, "The ranked map with the leaderboardId " + leaderBoardId + " already exists");
        }

        BeatSaverVersion beatSaverVersion = beatSaverSongInfo.getVersions().stream()
                .filter(version -> songHash.equalsIgnoreCase(version.getHash()))
                .findAny()
                .orElseThrow(() -> new AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, String.format("BeatSaver did not return requested map with hash %s", songHash)));

        Song song = this.songService.getOrCreateSong(beatSaverSongInfo, beatSaverVersion);

        BeatSaverMapDifficulty beatSaverMapDifficulty = beatSaverVersion.getDiffs().stream()
                .filter(diff -> difficulty.equalsIgnoreCase(diff.getDifficulty()) && "Standard".equalsIgnoreCase(diff.getCharacteristic()))
                .findAny().orElseThrow(() -> new AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND, String.format("Map with hash %s does not have [%s] difficulty as Standard characteristic", songHash, difficulty)));

        BeatMap beatMap = new BeatMap();
        beatMap.setLeaderboardId(leaderBoardId);
        beatMap.setMaxScore(this.calculateMaxScore(beatSaverMapDifficulty.getNotes()));
        beatMap.setSong(song);
        beatMap.setComplexity(complexity);
        beatMap.setDifficulty(difficulty);
        beatMap.setCategory(category);

        song.getBeatMaps().add(beatMap);

        song = this.songService.saveSong(song);

        this.scoreDataRepository.rankScores(leaderBoardId, beatMap.getMaxScore(), complexity);
    }

    private int mapDiffToId(String difficulty) {
        switch (difficulty.toLowerCase(Locale.ROOT)) {
            case "easy":
                return 1;
            case "normal":
                return 3;
            case "hard":
                return 5;
            case "expert":
                return 7;
            case "expertplus":
                return 9;
        }
        return -1;
    }

    public void removeRankedMap(Long leaderboardId) {
        Optional<BeatMap> map = this.beatMapRepository.findById(leaderboardId);
        if (map.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.RANKED_MAP_NOT_FOUND,
                    "The ranked map [" + leaderboardId + "] does not currently exist.");
        }

        BeatMap beatMap = map.get();
        Song song = beatMap.getSong();
        song.getBeatMaps().remove(beatMap);
        beatMap.setSong(null);
        if (song.getBeatMaps().size() == 0) {
            this.songService.removeSong(song.getSongHash());
        } else {
            this.songService.saveSong(song);
        }

        List<ScoreData> unrankedScores = this.scoreDataRepository.findAllByLeaderboardId(leaderboardId);
        unrankedScores.forEach(score -> score.setRankedScore(false));
        this.scoreDataRepository.saveAll(unrankedScores);
    }


    // There has to be a better way to do this
    private int calculateMaxScore(int noteCount) {
        int score = 0;
        for (int i = 1; i <= noteCount; i++) {

            if (i <= 1) {
                score += 1;
            } else if (i <= 5) {
                score += 2;
            } else if (i <= 13) {
                score += 4;
            } else {
                break;
            }
        }
        score *= 115;
        if (noteCount > 13) {
            score += (8 * (noteCount - 13)) * 115;
        }

        return score;
    }


}
