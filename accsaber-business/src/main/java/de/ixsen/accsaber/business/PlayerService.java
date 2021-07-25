package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.business.exceptions.FetchScoreException;
import de.ixsen.accsaber.business.mapping.BusinessMappingComponent;
import de.ixsen.accsaber.database.model.Category;
import de.ixsen.accsaber.database.model.PlayerCategoryStats;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.model.players.ScoreData;
import de.ixsen.accsaber.database.repositories.model.CategoryRepository;
import de.ixsen.accsaber.database.repositories.model.PlayerDataRepository;
import de.ixsen.accsaber.database.repositories.model.RankedMapRepository;
import de.ixsen.accsaber.database.repositories.model.ScoreDataRepository;
import de.ixsen.accsaber.database.repositories.view.OverallAccSaberPlayerRepository;
import de.ixsen.accsaber.database.views.AccSaberPlayer;
import de.ixsen.accsaber.integration.connector.ScoreSaberConnector;
import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberPlayerDto;
import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberScoreDto;
import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberScoreListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerService implements HasLogger {

    private final PlayerDataRepository playerDataRepository;
    private final ScoreSaberConnector scoreSaberConnector;
    private final BusinessMappingComponent mappingComponent;
    private final ScoreDataRepository scoreDataRepository;
    private final RankedMapRepository rankedMapRepository;
    private final String avatarFolder;
    private final CategoryRepository categoryRepository;
    private final OverallAccSaberPlayerRepository overallAccSaberPlayerRepository;

    @Autowired
    public PlayerService(PlayerDataRepository playerDataRepository,
                         OverallAccSaberPlayerRepository overallAccSaberPlayerRepository,
                         ScoreDataRepository scoreDataRepository,
                         RankedMapRepository rankedMapRepository,
                         ScoreSaberConnector scoreSaberConnector,
                         BusinessMappingComponent mappingComponent,
                         CategoryRepository categoryRepository,
                         @Value("${accsaber.image-save-location}") String imageFolder) {
        this.playerDataRepository = playerDataRepository;
        this.overallAccSaberPlayerRepository = overallAccSaberPlayerRepository;
        this.scoreDataRepository = scoreDataRepository;
        this.rankedMapRepository = rankedMapRepository;
        this.scoreSaberConnector = scoreSaberConnector;
        this.mappingComponent = mappingComponent;
        this.categoryRepository = categoryRepository;
        this.avatarFolder = imageFolder + "/avatars";
    }

    @PersistenceContext
    EntityManager em;

    @Transactional
    public List<AccSaberPlayer> getAllPlayers() {
        return this.overallAccSaberPlayerRepository.findAll();
    }

    public void signupPlayer(String playerId, String playerName, String hmd) {
        if (this.playerDataRepository.existsById(playerId)) {
            throw new AccsaberOperationException(ExceptionType.PLAYER_ALREADY_EXISTS, String.format("Player with ID %s already exists.", playerId));
        }

        PlayerData player = new PlayerData();
        player.setPlayerId(playerId);
        player.setPlayerName(playerName);
        player.setHmd(hmd);

        Set<PlayerCategoryStats> playerCategoryStatsList = this.categoryRepository.findAll().stream().map(category -> {
            PlayerCategoryStats playerCategoryStats = new PlayerCategoryStats();
            playerCategoryStats.setPlayer(player);
            playerCategoryStats.setCategory(category);
            return playerCategoryStats;
        }).collect(Collectors.toSet());

        player.setPlayerCategoryStats(playerCategoryStatsList);

        this.playerDataRepository.save(player);
    }

    public AccSaberPlayer getRankedPlayer(String playerId) {
        Optional<AccSaberPlayer> player = this.overallAccSaberPlayerRepository.findPlayerByPlayerId(playerId);
        if (player.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.PLAYER_NOT_FOUND, "Player with ID " + playerId + " does not exist.");
        }

        return player.get();
    }

    public PlayerData getPlayer(String playerId) {
        Optional<PlayerData> player = this.playerDataRepository.findById(playerId);
        if (player.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.PLAYER_NOT_FOUND, "Player with ID " + playerId + " does not exist.");
        }

        return player.get();
    }

    @Transactional
    public void loadPlayerScores() {
        List<PlayerData> allPlayers = this.playerDataRepository.findAll();
        Instant start = Instant.now();
        this.getLogger().info("Loading scores for " + allPlayers.size() + " players.");
        List<BeatMap> beatMaps = this.rankedMapRepository.findAll();

        for (PlayerData player : allPlayers) {
            this.handlePlayer(beatMaps, player);
        }
        this.getLogger().info("Loading scores finished in {} seconds.", Duration.between(start, Instant.now()).getSeconds());
    }

    protected void handlePlayer(List<BeatMap> beatMaps, PlayerData player) {
        Optional<ScoreSaberPlayerDto> optPlayerData = this.getScoreSaberPlayerData(player.getPlayerId());
        if (optPlayerData.isEmpty()) {
            this.getLogger().error("Couldn't fetch player with id {}, skipping", player.getPlayerId());
            return;
        }

        ScoreSaberPlayerDto playerData = optPlayerData.get();
        if (player.getAvatarUrl() == null) {
            this.loadAvatar(player.getPlayerId(), playerData.getPlayerInfo().getAvatar());
        }

        this.getLogger().trace("Loading {} scores for {}.", playerData.getScoreStats().getTotalPlayCount(), playerData.getPlayerInfo().getPlayerName());
        this.mappingComponent.getPlayerMapper().scoreSaberPlayerToPlayer(player, playerData.getPlayerInfo());
        int totalPlayCount = playerData.getScoreStats().getTotalPlayCount();

        int pageCount = (int) Math.ceil(totalPlayCount / 8.0);
        Iterator<ScoreData> scoreIterator = new ArrayList<>(player.getScores()).iterator();
        ScoreData score = null;
        if (scoreIterator.hasNext()) {
            score = scoreIterator.next();
        }

        List<ScoreData> newlySetScores = new ArrayList<>();
        for (int page = 1; page <= pageCount; page++) {
            this.getLogger().trace("Loading page {} for {}.", page, playerData.getPlayerInfo().getPlayerName());

            ScoreSaberScoreListDto scoreSaberScore = this.getScoreSaberScore(player.getPlayerId(), page);
            for (ScoreSaberScoreDto scoreSaberScoreDto : scoreSaberScore.getScores()) {
                boolean areScoreIdsIdentical = score != null && score.getScoreId() == scoreSaberScoreDto.getScoreId();
                if (areScoreIdsIdentical) {
                    if (Objects.equals(score.getTimeSet(), Instant.parse(scoreSaberScoreDto.getTimeSet()))) {
                        page = pageCount + 1;
                        break;
                    } else if (scoreIterator.hasNext()) {
                        score = scoreIterator.next();
                    } else {
                        score = null;
                    }
                }

                this.handleSetScores(beatMaps, player, newlySetScores, scoreSaberScoreDto);
            }
        }

        optPlayerData = this.getScoreSaberPlayerData(player.getPlayerId());
        if (optPlayerData.isPresent()) {
            int newMaxPage = (int) Math.ceil(playerData.getScoreStats().getTotalPlayCount() / 8.0);
            if (pageCount < newMaxPage && score == null) {
                this.getLogger().trace("Player {} has set a score that created a new page, while scores were loading, reloading latest page.", player.getPlayerName());
                ScoreSaberScoreListDto scoreSaberScore = this.getScoreSaberScore(player.getPlayerId(), newMaxPage);

                for (ScoreSaberScoreDto scoreSaberScoreDto : scoreSaberScore.getScores()) {
                    this.handleSetScores(beatMaps, player, newlySetScores, scoreSaberScoreDto);
                }
            }
        }

        this.scoreDataRepository.saveAll(newlySetScores);

        this.recalculateApForPlayer(player, this.categoryRepository.findAll());
        this.playerDataRepository.save(player);
    }

    private void handleSetScores(List<BeatMap> beatMaps, PlayerData player, List<ScoreData> newlySetScores, ScoreSaberScoreDto scoreSaberScoreDto) {
        Optional<ScoreData> optScore = player.getScores().stream().filter(score -> score.getScoreId() == scoreSaberScoreDto.getScoreId()).findFirst();

        ScoreData score;
        if (optScore.isPresent()) {
            score = this.mappingComponent.getScoreMapper().scoreSaberScoreDtoToExistingScore(optScore.get(), scoreSaberScoreDto);
        } else {
            score = this.mappingComponent.getScoreMapper().scoreSaberScoreDtoToScore(scoreSaberScoreDto);
            player.addScore(score);
        }

        this.handleRankedMaps(score, beatMaps, scoreSaberScoreDto);
        newlySetScores.add(score);
    }

    // fixme if necessary
    private void handleRankedMaps(ScoreData score, List<BeatMap> beatMaps, ScoreSaberScoreDto scoreSaberScoreDto) {
        Optional<BeatMap> potentialRankedMap = beatMaps.stream().filter(r -> Objects.equals(r.getLeaderboardId(), score.getLeaderboardId())).findFirst();

        if (potentialRankedMap.isPresent()) {
            BeatMap beatMap = potentialRankedMap.get();
            score.setAccuracy(scoreSaberScoreDto.getScore() / (double) beatMap.getMaxScore());

            double ap = APUtils.calculateApByAcc(score.getAccuracy(), beatMap.getComplexity());
            score.setAp(ap);
        }
    }

    @Transactional
    public void recalculateApForAllPlayers() {
        List<PlayerData> allPlayers = this.playerDataRepository.findAll();
        this.getLogger().trace("Recalculating ap for {} players.", allPlayers.size());
        List<Category> categories = this.categoryRepository.findAll();
        allPlayers.forEach(player -> this.recalculateApForPlayer(player, categories));
        this.playerDataRepository.saveAll(allPlayers);
    }

    public void loadAvatars() {
        Instant start = Instant.now();
        this.getLogger().info("Loading avatars..");
        this.playerDataRepository.findAll().forEach(player -> {
            if (player.getAvatarUrl() != null) {
                this.loadAvatar(player.getPlayerId(), player.getAvatarUrl());
            }
        });
        this.getLogger().info("Loading avatars finished in {} seconds.", Duration.between(start, Instant.now()).getSeconds());

    }

    /**
     * Loads the avatar of a player.
     */
    private void loadAvatar(String playerId, String avatarUrl) {
        byte[] avatar = this.scoreSaberConnector.loadAvatar(avatarUrl);
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.avatarFolder + "/" + playerId + ".jpg")) {
            fileOutputStream.write(avatar);
        } catch (IOException e) {
            this.getLogger().error("Unable to save avatar for player with playerId {}", playerId, e);
        }
    }

    private void recalculateApForPlayer(PlayerData player, List<Category> categories) {
        var rankedScoreCategoryMap = player.getScores().stream().filter(ScoreData::isRankedScore).collect(Collectors.groupingBy(score -> score.getBeatMap().getCategory()));
        for (Category category : categories) {
            double playerAp = 0.0f;
            double playerAccSum = 0.0f;
            PlayerCategoryStats playerCategoryStats = player.getPlayerCategoryStats()
                    .stream()
                    .filter(lP -> lP.getCategory().equals(category))
                    .findFirst()
                    .orElseGet(() -> {
                        PlayerCategoryStats newPlayerCategoryStats = new PlayerCategoryStats();
                        newPlayerCategoryStats.setCategory(category);
                        newPlayerCategoryStats.setPlayer(player);
                        player.getPlayerCategoryStats().add(newPlayerCategoryStats);
                        return newPlayerCategoryStats;
                    });

            if (rankedScoreCategoryMap.size() == 0) {
                playerCategoryStats.setAp(playerAp);
                playerCategoryStats.setAverageAcc(playerAccSum);
                return;
            }
            if (rankedScoreCategoryMap.containsKey(category)) {
                List<ScoreData> categoryScores = rankedScoreCategoryMap.get(category);
                for (ScoreData score : categoryScores) {
                    playerAp += score.getAp();
                    playerAccSum += score.getAccuracy();
                }

                playerCategoryStats.setAp(playerAp);
                playerCategoryStats.setAverageAcc(playerAccSum / categoryScores.size());
                playerCategoryStats.setRankedPlays(categoryScores.size());
            }
        }
    }

    private Optional<ScoreSaberPlayerDto> getScoreSaberPlayerData(String playerId) {
        return this.tryGetScoreSaberPlayerData(playerId, 0);
    }
    // TODO recheck whether recursion is the way to go here.

    private Optional<ScoreSaberPlayerDto> tryGetScoreSaberPlayerData(String playerId, int tryCount) {
        try {
            return this.scoreSaberConnector.getPlayerData(playerId);
        } catch (Exception e) {
            if (tryCount < 4) {
                this.getLogger().warn("Fetching failed for player with id " + playerId + ". Retrying", e);
                return this.tryGetScoreSaberPlayerData(playerId, tryCount + 1);
            } else {
                this.getLogger().error("Fetching failed after multiple tries. Scores will not update.", e);
                return Optional.empty();
            }
        }
    }

    private ScoreSaberScoreListDto getScoreSaberScore(String playerId, int page) {
        return this.tryGetScoreSaberScore(playerId, page, 0);
    }
    // TODO recheck whether recursion is the way to go here.

    private ScoreSaberScoreListDto tryGetScoreSaberScore(String playerId, int page, int tryCount) {
        try {
            return this.scoreSaberConnector.getPlayerScores(playerId, page);
        } catch (Exception e) {
            if (tryCount < 4) {
                this.getLogger().warn("Fetching scores failed for player with id " + playerId + ". Retrying", e);
                return this.tryGetScoreSaberScore(playerId, page, tryCount + 1);
            } else {
                throw new FetchScoreException("Fetching scores failed after multiple tries. Scores will not update.", e);
            }
        }
    }
}
