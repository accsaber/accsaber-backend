package de.ixsen.accsaber.business;

import de.ixsen.accsaber.business.exceptions.AccsaberOperationException;
import de.ixsen.accsaber.business.exceptions.ExceptionType;
import de.ixsen.accsaber.business.exceptions.FetchScoreException;
import de.ixsen.accsaber.business.mapping.BusinessMappingComponent;
import de.ixsen.accsaber.database.model.maps.RankedMap;
import de.ixsen.accsaber.database.model.players.Player;
import de.ixsen.accsaber.database.model.players.RankedPlayer;
import de.ixsen.accsaber.database.model.players.Score;
import de.ixsen.accsaber.database.repositories.PlayerRepository;
import de.ixsen.accsaber.database.repositories.RankedMapRepository;
import de.ixsen.accsaber.database.repositories.RankedPlayerRepository;
import de.ixsen.accsaber.database.repositories.ScoreRepository;
import de.ixsen.accsaber.integration.connector.ScoreSaberConnector;
import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberPlayerDto;
import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberScoreDto;
import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberScoreListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService implements HasLogger {

    private final PlayerRepository playerRepository;
    private final RankedPlayerRepository rankedPlayerRepository;
    private final ScoreSaberConnector scoreSaberConnector;
    private final BusinessMappingComponent mappingComponent;
    private final ScoreRepository scoreRepository;
    private final RankedMapRepository rankedMapRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository,
                         RankedPlayerRepository rankedPlayerRepository,
                         ScoreRepository scoreRepository,
                         RankedMapRepository rankedMapRepository,
                         ScoreSaberConnector scoreSaberConnector,
                         BusinessMappingComponent mappingComponent) {
        this.playerRepository = playerRepository;
        this.rankedPlayerRepository = rankedPlayerRepository;
        this.scoreRepository = scoreRepository;
        this.rankedMapRepository = rankedMapRepository;
        this.scoreSaberConnector = scoreSaberConnector;
        this.mappingComponent = mappingComponent;
    }

    @Transactional
    public List<RankedPlayer> getAllPlayers() {
        return this.rankedPlayerRepository.findAllWithRanking();
    }

    public void signupPlayer(String playerId, String playerName, String hmd) {
        if (this.playerRepository.existsById(playerId)) {
            throw new AccsaberOperationException(ExceptionType.PLAYER_ALREADY_EXISTS, "Player with ID " + playerId + " already exists.");
        }

        Player player = new Player();
        player.setPlayerId(playerId);
        player.setPlayerName(playerName);
        player.setHmd(hmd);

        this.playerRepository.save(player);
    }

    public RankedPlayer getRankedPlayer(String playerId) {
        Optional<RankedPlayer> player = this.rankedPlayerRepository.findPlayerByPlayerId(playerId);
        if (player.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.PLAYER_NOT_FOUND, "Player with ID " + playerId + " does not exist.");
        }

        return player.get();
    }

    public Player getPlayer(String playerId) {
        Optional<Player> player = this.playerRepository.findById(playerId);
        if (player.isEmpty()) {
            throw new AccsaberOperationException(ExceptionType.PLAYER_NOT_FOUND, "Player with ID " + playerId + " does not exist.");
        }

        return player.get();
    }

    public void loadPlayerScores() {
        List<Player> allPlayers = this.playerRepository.findAllWithScores();
        Instant start = Instant.now();
        this.getLogger().info("Loading scores for " + allPlayers.size() + " players.");
        List<RankedMap> rankedMaps = this.rankedMapRepository.findAll();

        for (Player player : allPlayers) {
            this.handlePlayer(rankedMaps, player);
        }
        this.getLogger().info("Loading scores finished in {} seconds.", Duration.between(start, Instant.now()).getSeconds());
    }

    @Transactional
    protected void handlePlayer(List<RankedMap> rankedMaps, Player player) {
        Optional<ScoreSaberPlayerDto> optPlayerData = this.getScoreSaberPlayerData(player.getPlayerId());
        if (optPlayerData.isEmpty()) {
            this.getLogger().error("Couldn't fetch player with id {}, skipping", player.getPlayerId());
            return;
        }

        ScoreSaberPlayerDto playerData = optPlayerData.get();
        this.getLogger().trace("Loading {} scores for {}.", playerData.getScoreStats().getTotalPlayCount(), playerData.getPlayerInfo().getPlayerName());
        this.mappingComponent.getPlayerMapper().scoreSaberPlayerToPlayer(player, playerData.getPlayerInfo());
        int totalPlayCount = playerData.getScoreStats().getTotalPlayCount();

        int pageCount = (int) Math.ceil(totalPlayCount / 8.0);
        Iterator<Score> scoreIterator = new ArrayList<>(player.getScores()).iterator();
        Score score = null;
        if (scoreIterator.hasNext()) {
            score = scoreIterator.next();
        }

        List<Score> newlySetScores = new ArrayList<>();
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

                this.handleSetScores(rankedMaps, player, newlySetScores, scoreSaberScoreDto);
            }
        }

        optPlayerData = this.getScoreSaberPlayerData(player.getPlayerId());
        if (optPlayerData.isPresent()) {
            int newMaxPage = (int) Math.ceil(playerData.getScoreStats().getTotalPlayCount() / 8.0);
            if (pageCount < newMaxPage && score == null) {
                this.getLogger().trace("Player {} has set a score that created a new page, while scores were loading, reloading latest page.", player.getPlayerName());
                ScoreSaberScoreListDto scoreSaberScore = this.getScoreSaberScore(player.getPlayerId(), newMaxPage);

                for (ScoreSaberScoreDto scoreSaberScoreDto : scoreSaberScore.getScores()) {
                    this.handleSetScores(rankedMaps, player, newlySetScores, scoreSaberScoreDto);
                }
            }
        }

        this.scoreRepository.saveAll(newlySetScores);
        this.recalculateApForPlayer(player);
        this.playerRepository.save(player);
    }

    private void handleSetScores(List<RankedMap> rankedMaps, Player player, List<Score> newlySetScores, ScoreSaberScoreDto scoreSaberScoreDto) {
        Optional<Score> optScore = player.getScores().stream().filter(score -> score.getScoreId() == scoreSaberScoreDto.getScoreId()).findFirst();

        Score score;
        if (optScore.isPresent()) {
            score = this.mappingComponent.getScoreMapper().scoreSaberScoreDtoToExistingScore(optScore.get(), scoreSaberScoreDto);
        } else {
            score = this.mappingComponent.getScoreMapper().scoreSaberScoreDtoToScore(scoreSaberScoreDto);
            player.addScore(score);
        }

        this.handleRankedMaps(score, rankedMaps, scoreSaberScoreDto);
        newlySetScores.add(score);
    }

    // fixme if necessary
    private void handleRankedMaps(Score score, List<RankedMap> rankedMaps, ScoreSaberScoreDto scoreSaberScoreDto) {
        Optional<RankedMap> potentialRankedMap = rankedMaps.stream().filter(r -> Objects.equals(r.getLeaderboardId(), score.getLeaderboardId())).findFirst();

        if (potentialRankedMap.isPresent()) {
            score.setIsRankedMapScore(true);
            score.setAccuracy(scoreSaberScoreDto.getScore() / (double) potentialRankedMap.get().getMaxScore());

            double ap = APUtils.calculateApByAcc(score.getAccuracy(), potentialRankedMap.get().getTechyness());
            score.setAp(ap);
        }
    }

    public void recalculateApForAllPlayers() {
        List<Player> allPlayers = this.playerRepository.findAllWithScores();
        this.getLogger().trace("Recalculating ap for " + allPlayers.size() + " players.");
        allPlayers.forEach(this::recalculateApForPlayer);
        this.playerRepository.saveAll(allPlayers);
    }

    private void recalculateApForPlayer(Player player) {
        double playerAp = 0.0f;
        double playerAccSum = 0.0f;
        List<Score> rankedScores = player.getScores().stream().filter(Score::getIsRankedMapScore).collect(Collectors.toList());
        if (rankedScores.size() == 0) {
            player.setAp(playerAp);
            player.setAverageApPerMap(playerAp);
            player.setRankedPlays(0);
            player.setAverageAcc(playerAccSum);
            return;
        }
        for (Score score : rankedScores) {
            playerAp += score.getAp();
            playerAccSum += score.getAccuracy();
        }
        player.setAp(playerAp);
        player.setAverageApPerMap(playerAp / rankedScores.size());
        player.setAverageAcc(playerAccSum / rankedScores.size());
        player.setRankedPlays(rankedScores.size());
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
