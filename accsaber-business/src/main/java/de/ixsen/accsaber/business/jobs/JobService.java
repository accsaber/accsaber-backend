package de.ixsen.accsaber.business.jobs;

import de.ixsen.accsaber.business.HasLogger;
import de.ixsen.accsaber.business.PlayerService;
import de.ixsen.accsaber.business.ScoreService;
import de.ixsen.accsaber.business.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@EnableScheduling
public class JobService implements HasLogger {

    private final PlayerService playerService;
    private final ScoreService scoreService;
    private final SongService songService;
    private final boolean disableScoreFetching;
    private final boolean disableAvatarFetching;
    private final boolean recalculateApOnStartup;
    private final boolean reloadSongCoversOnStartup;


    @Autowired
    public JobService(PlayerService playerService, ScoreService scoreService, SongService songService,
                      @Value("${accsaber.disable-score-fetching}") boolean disableScoreFetching,
                      @Value("${accsaber.disable-avatar-fetching}") boolean disableAvatarFetching,
                      @Value("${accsaber.recalculate-ap-on-startup}") boolean recalculateApOnStartup,
                      @Value("${accsaber.reload-song-covers-on-startup}") boolean reloadSongCoversOnStartup) {
        this.playerService = playerService;
        this.scoreService = scoreService;
        this.songService = songService;
        this.disableScoreFetching = disableScoreFetching;
        this.disableAvatarFetching = disableAvatarFetching;
        this.recalculateApOnStartup = recalculateApOnStartup;
        this.reloadSongCoversOnStartup = reloadSongCoversOnStartup;
    }

    @PostConstruct
    public void onStartUpDone() {
        if (this.recalculateApOnStartup) {
            this.getLogger().info("Recalculating all AP values...");
            this.scoreService.recalculateApForAllScores();
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
            this.playerService.loadPlayerScores();
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
}
