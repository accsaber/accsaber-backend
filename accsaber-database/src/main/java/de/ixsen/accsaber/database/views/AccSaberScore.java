package de.ixsen.accsaber.database.views;


import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.views.extenders.WithRank;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import java.time.Instant;

@Entity
@Immutable
public class AccSaberScore extends WithRank {

    @Id
    private Long scoreId;

    private int rankWhenScoresSet;

    private int score;

    private int unmodififiedScore;

    private Double accuracy;

    private Double ap;

    @OrderBy
    private Instant timeSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false, name = "map_leaderboard_id")
    private BeatMap beatMap;

    @Column(name = "map_leaderboard_id")
    private Long leaderboardId;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerData player;

    public Long getScoreId() {
        return scoreId;
    }

    public int getRankWhenScoresSet() {
        return rankWhenScoresSet;
    }

    public int getScore() {
        return score;
    }

    public int getUnmodififiedScore() {
        return unmodififiedScore;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public Double getAp() {
        return ap;
    }

    public Instant getTimeSet() {
        return timeSet;
    }

    public BeatMap getBeatMap() {
        return beatMap;
    }

    public Long getLeaderboardId() {
        return leaderboardId;
    }

    public PlayerData getPlayer() {
        return player;
    }
}
