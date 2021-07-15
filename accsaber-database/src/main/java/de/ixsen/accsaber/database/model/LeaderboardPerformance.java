package de.ixsen.accsaber.database.model;

import de.ixsen.accsaber.database.model.players.Player;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class LeaderboardPerformance extends BaseEntity {
    @ManyToOne
    private Player player;

    @ManyToOne
    private Leaderboard leaderboard;

    private Double averageAcc;

    private Double ap;
    private Double averageApPerMap;
    private int rankedPlays;

    @ManyToMany
    private List<RankHistoryEntry> leaderboardHistory;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    public Double getAverageAcc() {
        return averageAcc;
    }

    public void setAverageAcc(Double averageAcc) {
        this.averageAcc = averageAcc;
    }

    public Double getAp() {
        return ap;
    }

    public void setAp(Double ap) {
        this.ap = ap;
    }

    public Double getAverageApPerMap() {
        return averageApPerMap;
    }

    public void setAverageApPerMap(Double averageApPerMap) {
        this.averageApPerMap = averageApPerMap;
    }

    public int getRankedPlays() {
        return rankedPlays;
    }

    public void setRankedPlays(int rankedPlays) {
        this.rankedPlays = rankedPlays;
    }

    public List<RankHistoryEntry> getLeaderboardHistory() {
        return leaderboardHistory;
    }

    public void setLeaderboardHistory(List<RankHistoryEntry> leaderboardHistory) {
        this.leaderboardHistory = leaderboardHistory;
    }
}
