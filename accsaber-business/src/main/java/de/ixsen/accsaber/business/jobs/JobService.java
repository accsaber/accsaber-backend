package de.ixsen.accsaber.business.jobs;

import de.ixsen.accsaber.business.HasLogger;
import de.ixsen.accsaber.business.PlayerService;
import de.ixsen.accsaber.business.SongService;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.repositories.model.BeatMapRepository;
import de.ixsen.accsaber.integration.connector.ScoreSaberConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
@EnableScheduling
public class JobService implements HasLogger {

    private final PlayerService playerService;
    private final boolean disableScoreFetching;
    private final boolean disableAvatarFetching;
    private final boolean recalculateApOnStartup;
    private final boolean reloadSongCoversOnStartup;
    private final BeatMapRepository beatMapRepository;
    private final SongService songService;
    private final boolean disableRankSnapshots;
    private final int fetchThreadPool;


    @Autowired
    public JobService(PlayerService playerService,
                      SongService songService,
                      BeatMapRepository beatMapRepository,
                      @Value("${accsaber.disable-score-fetching}") boolean disableScoreFetching,
                      @Value("${accsaber.disable-avatar-fetching}") boolean disableAvatarFetching,
                      @Value("${accsaber.disable-rank-snapshots}") boolean disableRankSnapshots,
                      @Value("${accsaber.recalculate-ap-on-startup}") boolean recalculateApOnStartup,
                      @Value("${accsaber.reload-song-covers-on-startup}") boolean reloadSongCoversOnStartup,
                      @Value("${accsaber.fetch-thread-pool}") int fetchThreadPool) {
        this.playerService = playerService;
        this.songService = songService;
        this.beatMapRepository = beatMapRepository;
        this.disableScoreFetching = disableScoreFetching;
        this.disableRankSnapshots = disableRankSnapshots;
        this.disableAvatarFetching = disableAvatarFetching;
        this.recalculateApOnStartup = recalculateApOnStartup;
        this.reloadSongCoversOnStartup = reloadSongCoversOnStartup;
        this.fetchThreadPool = fetchThreadPool;
    }

    @PostConstruct
    public void onStartUpDone() {
        if (this.recalculateApOnStartup) {
            this.getLogger().info("Recalculating all AP values...");
            this.playerService.recalculateApForAllPlayers();
        }
        if (this.reloadSongCoversOnStartup) {
            this.songService.reloadAllCovers();
        }
    }

    @Scheduled(fixedDelayString = "${accsaber.score-fetch-intervall-millis}")
    public void loadPlayerScores() {
        if (this.disableScoreFetching) {
            this.getLogger().warn("Score fetching is disabled.");
        } else {
            List<Long> allPlayerIds = this.playerService.loadAllPlayerIds();

            Instant start = Instant.now();
            this.getLogger().info("Loading scores for " + allPlayerIds.size() + " players.");
            List<BeatMap> allRankedMaps = this.beatMapRepository.findAll();

            ExecutorService execService = Executors.newFixedThreadPool(fetchThreadPool);
            Set<Future<?>> futures = ConcurrentHashMap.newKeySet();
            for (int i = 0; i < allPlayerIds.size(); i++) {

                int playerIdIt = i;

                Future<?> future = execService.submit(() -> this.playerService.handlePlayer(allRankedMaps, allPlayerIds.get(playerIdIt)));
                futures.add(future);

            }

            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    this.getLogger().error("Exception while waiting for player fetching", e);
                }
            }

            long duration = Duration.between(start, Instant.now()).getSeconds();
            long minutes = duration / 60;
            long seconds = duration % 60;
            this.getLogger().info("Loading scores finished in {} minutes and {} seconds.", minutes, seconds);
            this.getLogger().info("Maximum amount of fetches per minute was {} with a thread pool of {}.", ScoreSaberConnector.getMax(), fetchThreadPool);
        }
    }

    @Scheduled(fixedDelayString = "${accsaber.avatar-fetch-intervall-millis}")
    public void loadAvatars() {
        if (this.disableAvatarFetching) {
            this.getLogger().warn("Avatar fetching is disabled.");
        } else {
            this.playerService.loadAvatars();
        }
    }

    /**
     * This method calls the database procedure for taking a snapshot of the current ranks. Current practice is to call this method more than once a day so that
     */
    @Scheduled(fixedDelayString = "${accsaber.rank-snapshot-intervall-millis}")
    public void takeRankingSnapshot() {
        if (this.disableRankSnapshots) {
            this.getLogger().warn("Rank snapshot are disabled");
        } else {
            this.playerService.takeRankingSnapshot();
        }
    }
}
