package de.ixsen.accsaber.database.model.players;


import de.ixsen.accsaber.database.model.maps.BeatMap;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Audited
public class ScoreData implements Serializable {

    @Id
    @Audited
    private Long scoreId;

    private int rankWhenScoresSet;

    @Audited
    private int score;

    @Audited
    private int unmodififiedScore;

    @Audited
    private Double accuracy;

    @Audited
    private Double ap;

    @OrderBy
    @Audited
    private Instant timeSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotAudited
    @JoinColumn(insertable = false, updatable = false, name = "map_leaderboard_id")
    private BeatMap beatMap;

    @Column(name = "map_leaderboard_id")
    @Audited
    private Long leaderboardId;

    @ManyToOne
    @NotAudited
    @JoinColumn(name = "player_id")
    private PlayerData player;

    @Column(insertable = false, updatable = false)
    private Long rank;

    public Long getrank() {
        return this.rank;
    }

    public void setrank(Long rank) {
        this.rank = rank;
    }

    public Long getScoreId() {
        return this.scoreId;
    }

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

    public int getRankWhenScoresSet() {
        return this.rankWhenScoresSet;
    }

    public void setRankWhenScoresSet(int rankWhenScoresSet) {
        this.rankWhenScoresSet = rankWhenScoresSet;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getUnmodififiedScore() {
        return this.unmodififiedScore;
    }

    public void setUnmodififiedScore(int unmodififiedScore) {
        this.unmodififiedScore = unmodififiedScore;
    }

    public Double getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Instant getTimeSet() {
        return this.timeSet;
    }

    public void setTimeSet(Instant timeSet) {
        this.timeSet = timeSet;
    }

    public BeatMap getMap() {
        return this.beatMap;
    }

    public void setRankedMap(BeatMap map) {
        this.beatMap = map;
    }

    public Long getLeaderboardId() {
        return this.leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public Double getAp() {
        return this.ap;
    }

    public void setAp(Double ap) {
        this.ap = ap;
    }

    public PlayerData getPlayer() {
        return this.player;
    }

    public void setPlayer(PlayerData player) {
        this.player = player;
    }
}
