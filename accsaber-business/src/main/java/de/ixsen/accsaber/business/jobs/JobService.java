package de.ixsen.accsaber.business.jobs;

import de.ixsen.accsaber.business.HasLogger;
import de.ixsen.accsaber.business.PlayerService;
import de.ixsen.accsaber.business.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class JobService implements HasLogger {

    private final PlayerService playerService;
    private final ScoreService scoreService;
    private final boolean disableScoreFetching;


    @Autowired
    public JobService(PlayerService playerService, ScoreService scoreService, @Value("${accsaber.disable-score-fetching}") boolean disableScoreFetching) {
        this.playerService = playerService;
        this.scoreService = scoreService;
        this.disableScoreFetching = disableScoreFetching;

        this.scoreService.recalculateApForAllScores();
        this.playerService.recalculateApForAllPlayers();
    }

    @Scheduled(fixedDelayString = "${accsaber.score-fetch-intervall-seconds}")
    public void executeJob() {
        if (this.disableScoreFetching) {
            this.getLogger().warn("Score fetching is disabled.");
        } else {
            this.playerService.loadPlayerScores();
        }
    }
}
