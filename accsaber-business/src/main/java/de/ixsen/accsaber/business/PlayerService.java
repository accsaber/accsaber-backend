package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.business.exceptions.FetchScoreException;
import de.ixsen.accsaber.business.mapping.BusinessMappingComponent;
import de.ixsen.accsaber.database.model.Category;
import de.ixsen.accsaber.database.model.PlayerCategoryStats;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.model.players.PlayerRankHistory;
import de.ixsen.accsaber.database.model.players.ScoreData;
import de.ixsen.accsaber.database.repositories.model.CategoryRepository;
import de.ixsen.accsaber.database.repositories.model.PlayerDataRepository;
import de.ixsen.accsaber.database.repositories.model.PlayerRankHistoryRepository;
import de.ixsen.accsaber.database.repositories.model.ScoreDataRepository;
import de.ixsen.accsaber.database.repositories.view.CategoryAccSaberPlayerRepository;
import de.ixsen.accsaber.database.repositories.view.OverallAccSaberPlayerRepository;
import de.ixsen.accsaber.database.views.AccSaberPlayer;
import de.ixsen.accsaber.integration.connector.ScoreSaberConnector;
import de.ixsen.accsaber.integration.model.scoresaber.player.ScoreSaberPlayerDto;
import de.ixsen.accsaber.integration.model.scoresaber.score.ScoreSaberScoreBundleDto;
import de.ixsen.accsaber.integration.model.scoresaber.score.ScoreSaberScoreListDto;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    private final String avatarFolder;
    private final CategoryRepository categoryRepository;
    private final OverallAccSaberPlayerRepository overallAccSaberPlayerRepository;
    private final PlayerRankHistoryRepository playerRankHistoryRepository;
    private final CategoryAccSaberPlayerRepository categoryAccSaberPlayerRepository;

    @Autowired
    public PlayerService(PlayerDataRepository playerDataRepository,
                         OverallAccSaberPlayerRepository overallAccSaberPlayerRepository,
                         CategoryAccSaberPlayerRepository categoryAccSaberPlayerRepository,
                         PlayerRankHistoryRepository playerRankHistoryRepository,
                         ScoreDataRepository scoreDataRepository,
                         ScoreSaberConnector scoreSaberConnector,
                         BusinessMappingComponent mappingComponent,
                         CategoryRepository categoryRepository,
                         @Value("${accsaber.image-save-location}") String imageFolder) {
        this.playerDataRepository = playerDataRepository;
        this.overallAccSaberPlayerRepository = overallAccSaberPlayerRepository;
        this.categoryAccSaberPlayerRepository = categoryAccSaberPlayerRepository;
        this.playerRankHistoryRepository = playerRankHistoryRepository;
        this.scoreDataRepository = scoreDataRepository;
        this.scoreSaberConnector = scoreSaberConnector;
        this.mappingComponent = mappingComponent;
        this.categoryRepository = categoryRepository;
        this.avatarFolder = imageFolder + "/avatars";
    }

    @Transactional
    public List<AccSaberPlayer> getAllPlayers() {
        return this.overallAccSaberPlayerRepository.findAll();
    }

    public void signupPlayer(Long playerId, String playerName, String hmd) {
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

    public Optional<AccSaberPlayer> getRankedPlayer(long playerId) {
        return this.overallAccSaberPlayerRepository.findPlayerByPlayerId(playerId);
    }

    public Optional<AccSaberPlayer> getRankedPlayerForCategory(long playerId, String category) {
        if (Objects.equals(category, "overall")) {
            return this.getRankedPlayer(playerId);
        }
        return this.categoryAccSaberPlayerRepository.findPlayerByPlayerIdAndCategoryName(playerId, category);
    }

    public PlayerData getPlayer(Long playerId) {
        Optional<PlayerData> player = this.playerDataRepository.findById(playerId);
        if (player.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.PLAYER_NOT_FOUND, "Player with ID " + playerId + " does not exist.");
        }

        return player.get();
    }

    @Transactional
    public List<Long> loadAllPlayerIds() {
        return this.playerDataRepository.findAllPlayerIds();
    }

    @Transactional
    public void handlePlayer(List<BeatMap> allRankedMaps, Long playerId) {
        Optional<PlayerData> optPlayer = this.playerDataRepository.findById(playerId);
        if (optPlayer.isEmpty()) {
            this.getLogger().warn("Previously loaded player with id [{}] was not found in the database", playerId);
            return;
        }
        PlayerData player = optPlayer.get();
        Hibernate.initialize(player.getScores());

        Optional<ScoreSaberPlayerDto> optPlayerData = this.getScoreSaberPlayerData(player.getPlayerId());
        if (optPlayerData.isEmpty()) {
            this.getLogger().error("Couldn't fetch player with id {}, skipping", player.getPlayerId());
            return;
        }

        ScoreSaberPlayerDto playerData = optPlayerData.get();
        if (player.getAvatarUrl() == null) {
            this.loadAvatar(player.getPlayerId(), playerData.getProfilePicture());
        }

        this.getLogger().info("Loading {} scores for {}.", playerData.getScoreStats().getTotalPlayCount(), playerData.getName());
        this.mappingComponent.getPlayerMapper().scoreSaberPlayerToPlayer(player, playerData);
        int totalPlayCount = playerData.getScoreStats().getTotalPlayCount();

        int pageCount = (int) Math.ceil(totalPlayCount / 100.0);
        Iterator<ScoreData> scoreIterator = new ArrayList<>(player.getScores()).iterator();
        ScoreData score = null;
        if (scoreIterator.hasNext()) {
            score = scoreIterator.next();
        }

        List<ScoreData> newlySetScores = new ArrayList<>();
        for (int page = 1; page <= pageCount; page++) {
            this.getLogger().trace("Loading page {} for {}.", page, playerData.getName());

            ScoreSaberScoreListDto scoreSaberScores = this.getScoreSaberScore(player.getPlayerId(), page);
            for (ScoreSaberScoreBundleDto scoreSaberScoreBundleDto : scoreSaberScores.getPlayerScores()) {
                boolean areScoreIdsIdentical = score != null && score.getScoreId() == scoreSaberScoreBundleDto.getScore().getId();
                if (areScoreIdsIdentical) {
                    if (Objects.equals(score.getTimeSet(), Instant.parse(scoreSaberScoreBundleDto.getScore().getTimeSet()))) {
                        page = pageCount + 1;
                        break;
                    } else if (scoreIterator.hasNext()) {
                        score = scoreIterator.next();
                    } else {
                        score = null;
                    }
                }

                this.handleSetScores(player, newlySetScores, scoreSaberScoreBundleDto);
            }
        }

        optPlayerData = this.getScoreSaberPlayerData(player.getPlayerId());
        if (optPlayerData.isPresent()) {
            int newMaxPage = (int) Math.ceil(playerData.getScoreStats().getTotalPlayCount() / 100.0);
            if (pageCount < newMaxPage && score == null) {
                this.getLogger().trace("Player {} has set a score that created a new page, while scores were loading, reloading latest page.", player.getPlayerName());
                ScoreSaberScoreListDto scoreSaberScores = this.getScoreSaberScore(player.getPlayerId(), newMaxPage);

                for (ScoreSaberScoreBundleDto scoreSaberScoreBundleDto : scoreSaberScores.getPlayerScores()) {
                    this.handleSetScores(player, newlySetScores, scoreSaberScoreBundleDto);
                }
            }
        }

        this.scoreDataRepository.saveAll(newlySetScores);
        this.playerDataRepository.save(player);

        this.scoreDataRepository.flush();
        this.playerDataRepository.flush();

        List<Long> categoryIds = newlySetScores.stream()
                .filter(tuple -> allRankedMaps.stream().anyMatch(map -> map.getLeaderboardId().equals(tuple.getLeaderboardId())))
                .map(scoreTuple -> allRankedMaps.stream().filter(map -> map.getLeaderboardId().equals(scoreTuple.getLeaderboardId())).findFirst().get().getCategory().getId())
                .distinct()
                .collect(Collectors.toList());

        if (newlySetScores.isEmpty()) {
            return;
        }

        this.getLogger().trace(String.format("Recalculating player AP for %s after successfully loading new scores", player.getPlayerName()));
        this.playerDataRepository.recalcPlayerAp(player.getPlayerId());

        if (categoryIds.size() == 3) {
            this.playerDataRepository.recalculatePlayerCategoryStats(player.getPlayerId());
        } else {
            for (Long categoryId : categoryIds) {
                this.playerDataRepository.recalculatePlayerCategoryStat(player.getPlayerId(), categoryId);
            }
        }
    }

    private void handleSetScores(PlayerData player, List<ScoreData> newlySetScores, ScoreSaberScoreBundleDto scoreSaberScoreBundleDto) {
        Optional<ScoreData> optScore = player.getScores().stream().filter(score -> score.getScoreId() == scoreSaberScoreBundleDto.getScore().getId()).findFirst();

        ScoreData score;
        if (optScore.isPresent()) {
            score = this.mappingComponent.getScoreMapper().scoreSaberScoreDtoToExistingScore(optScore.get(), scoreSaberScoreBundleDto);
        } else {
            score = this.mappingComponent.getScoreMapper().scoreSaberScoreDtoToScore(scoreSaberScoreBundleDto);
            player.addScore(score);
        }

        newlySetScores.add(score);
    }


    @Transactional
    public void recalculateApForAllPlayers() {
        this.playerDataRepository.recalculateAllAp();
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

    public void takeRankingSnapshot() {
        this.getLogger().trace("Taking snapshot of current ranks");
        this.playerDataRepository.takeRankSnapshot();
    }

    public List<PlayerRankHistory> getRecentPlayerRankHistory(Long playerId) {
        return this.playerRankHistoryRepository.findLastMonthForPlayer(playerId);
    }

    public List<PlayerRankHistory> getRecentPlayerRankHistoryForCategory(Long playerId, String categoryName) {
        Category category = this.categoryRepository.findByCategoryName(categoryName).orElseThrow(() -> new AccsaberOperationException(ExceptionType.CATEGORY_NOT_FOUND, String.format("Category [%s] does not exist.", categoryName)));
        return this.playerRankHistoryRepository.findLastMonthForPlayerAndCategory(playerId, category.getId());
    }

    /**
     * Loads the avatar of a player.
     */
    private void loadAvatar(Long playerId, String avatarUrl) {
        byte[] avatar = this.scoreSaberConnector.loadAvatar(avatarUrl);
        try (FileOutputStream fileOutputStream = new FileOutputStream(this.avatarFolder + "/" + playerId + ".jpg")) {
            fileOutputStream.write(avatar);
        } catch (IOException e) {
            this.getLogger().error("Unable to save avatar for player with playerId {}", playerId, e);
        }
    }

    private Optional<ScoreSaberPlayerDto> getScoreSaberPlayerData(Long playerId) {
        return this.tryGetScoreSaberPlayerData(playerId, 0);
    }

    // TODO recheck whether recursion is the way to go here.
    private Optional<ScoreSaberPlayerDto> tryGetScoreSaberPlayerData(Long playerId, int tryCount) {
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

    private ScoreSaberScoreListDto getScoreSaberScore(Long playerId, int page) {
        return this.tryGetScoreSaberScore(playerId, page, 0);
    }

    // TODO recheck whether recursion is the way to go here.
    private ScoreSaberScoreListDto tryGetScoreSaberScore(Long playerId, int page, int tryCount) {
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
