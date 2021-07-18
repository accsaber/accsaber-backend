package de.ixsen.accsaber.database.model;

import de.ixsen.accsaber.database.model.players.Player;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class CategoryPerformance extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "category_name")
    private Category category;

    private Double averageAcc;

    private Double ap;
    private Double averageApPerMap;

    @ColumnDefault("0")
    private int rankedPlays;

    @Column(insertable = false, updatable = false)
    @Transient
    private Long rank;

    // TODO -> rank history for each category?
    @ManyToMany
    private List<RankHistoryEntry> leaderboardHistory;

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getAverageAcc() {
        return this.averageAcc;
    }

    public void setAverageAcc(Double averageAcc) {
        this.averageAcc = averageAcc;
    }

    public Double getAp() {
        return this.ap;
    }

    public void setAp(Double ap) {
        this.ap = ap;
    }

    public Double getAverageApPerMap() {
        return this.averageApPerMap;
    }

    public void setAverageApPerMap(Double averageApPerMap) {
        this.averageApPerMap = averageApPerMap;
    }

    public int getRankedPlays() {
        return this.rankedPlays;
    }

    public void setRankedPlays(int rankedPlays) {
        this.rankedPlays = rankedPlays;
    }

    public List<RankHistoryEntry> getLeaderboardHistory() {
        return this.leaderboardHistory;
    }

    public void setLeaderboardHistory(List<RankHistoryEntry> leaderboardHistory) {
        this.leaderboardHistory = leaderboardHistory;
    }

    public Long getRank() {
        return this.rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }
}
