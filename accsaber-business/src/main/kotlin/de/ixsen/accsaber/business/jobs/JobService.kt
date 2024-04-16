package de.ixsen.accsaber.business.jobs

import de.ixsen.accsaber.business.HasLogger
import de.ixsen.accsaber.business.PlayerService
import de.ixsen.accsaber.business.SongService
import de.ixsen.accsaber.database.repositories.model.BeatMapRepository
import de.ixsen.accsaber.integration.connector.ScoreSaberConnector
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.Future
import javax.annotation.PostConstruct

@Service
@EnableScheduling
class JobService
@Autowired constructor(
    private val playerService: PlayerService,
    private val songService: SongService,
    private val beatMapRepository: BeatMapRepository,
    @Value("\${accsaber.disable-score-fetching}") private val disableScoreFetching: Boolean,
    @Value("\${accsaber.disable-avatar-fetching}") private val disableAvatarFetching: Boolean,
    @Value("\${accsaber.disable-rank-snapshots}") private val disableRankSnapshots: Boolean,
    @Value("\${accsaber.recalculate-ap-on-startup}") private val recalculateApOnStartup: Boolean,
    @Value("\${accsaber.reload-song-covers-on-startup}") private val reloadSongCoversOnStartup: Boolean,
    @Value("\${accsaber.fetch-thread-pool}") private val fetchThreadPool: Int
) : HasLogger {
    @PostConstruct
    fun onStartUpDone() {
        if (recalculateApOnStartup) {
            this.getLogger().info("Recalculating all AP values...")
            playerService.recalculateApForAllPlayers()
        }
        if (reloadSongCoversOnStartup) {
            songService.reloadAllCovers()
        }
    }

    @Scheduled(fixedDelayString = "\${accsaber.score-fetch-intervall-millis}")
    fun loadPlayerScores() {
        if (disableScoreFetching) {
            this.getLogger().warn("Score fetching is disabled.")
        } else {
            val start = Instant.now()
            val execService = Executors.newFixedThreadPool(fetchThreadPool)
            val futures: MutableSet<Future<*>> = ConcurrentHashMap.newKeySet()

            val allPlayerIds = playerService.loadAllPlayerIds()
            val allRankedMaps = beatMapRepository.findAll()

            this.getLogger().info("Loading scores for " + allPlayerIds.size + " players.")
            for (i in allPlayerIds.indices) {
                val future = execService.submit { playerService.handlePlayer(allRankedMaps, allPlayerIds[i]) }
                futures.add(future)
            }

            for (future in futures) {
                try {
                    future.get()
                } catch (e: InterruptedException) {
                    this.getLogger().error("Exception while waiting for player fetching", e)
                } catch (e: ExecutionException) {
                    this.getLogger().error("Exception while waiting for player fetching", e)
                }
            }

            this.playerService.refreshMaterializedViews()

            val duration = Duration.between(start, Instant.now()).seconds
            val minutes = duration / 60
            val seconds = duration % 60
            this.getLogger().info("Loading scores finished in {} minutes and {} seconds.", minutes, seconds)
            this.getLogger().info("Maximum amount of fetches per minute was {} with a thread pool of {}.", ScoreSaberConnector.maxRequestsThisSession, fetchThreadPool)
        }
    }

    @Scheduled(fixedDelayString = "\${accsaber.avatar-fetch-intervall-millis}")
    fun loadAvatars() {
        if (disableAvatarFetching) {
            this.getLogger().warn("Avatar fetching is disabled.")
        } else {
            playerService.loadAvatars()
        }
    }

    /**
     * This method calls the database procedure for taking a snapshot of the current ranks. Current practice is to call this method more than once a day so that
     */
    @Scheduled(fixedDelayString = "\${accsaber.rank-snapshot-intervall-millis}")
    fun takeRankingSnapshot() {
        if (disableRankSnapshots) {
            this.getLogger().warn("Rank snapshot are disabled")
        } else {
            playerService.takeRankingSnapshot()
        }
    }
}
