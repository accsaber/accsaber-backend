package de.ixsen.accsaber.database.model.players;

import de.ixsen.accsaber.database.model.maps.RankedMap;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;
import java.time.Instant;

@MappedSuperclass
public class AbstractScore {

    @Id
    private Long scoreId;

    private int rankWhenScoresSet;
    private int score;
    private int unmodififiedScore;

    private Double accuracy;
    private Double ap;

    @Column(name = "is_ranked_map_score")
    private Boolean isRankedMapScore = false;

    @OrderBy
    private Instant timeSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotAudited
    @JoinColumn(insertable = false, updatable = false, name = "map_leaderboard_id")
    private RankedMap rankedMap;

    @Column(name = "map_leaderboard_id")
    private Long leaderboardId;

    @ManyToOne
    @NotAudited
    private Player player;


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

    public RankedMap getMap() {
        return this.rankedMap;
    }

    public void setMap(RankedMap map) {
        this.rankedMap = map;
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

    public Boolean getIsRankedMapScore() {
        return this.isRankedMapScore;
    }

    public void setIsRankedMapScore(Boolean rankedMapScore) {
        this.isRankedMapScore = rankedMapScore;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
