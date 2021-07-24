package de.ixsen.accsaber.database.model;

import de.ixsen.accsaber.database.model.players.PlayerData;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class PlayerCategoryStats extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerData player;

    @ManyToOne
    @JoinColumn(name = "category_name")
    private Category category;

    private Double averageAcc;

    private Double ap;

    @ColumnDefault("0")
    private int rankedPlays;

//    @ManyToMany
//    private List<RankHistoryEntry> leaderboardHistory;

    public PlayerData getPlayer() {
        return this.player;
    }

    public void setPlayer(PlayerData player) {
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

    public int getRankedPlays() {
        return this.rankedPlays;
    }

    public void setRankedPlays(int rankedPlays) {
        this.rankedPlays = rankedPlays;
    }

//    public List<RankHistoryEntry> getLeaderboardHistory() {
//        return this.leaderboardHistory;
//    }
//
//    public void setLeaderboardHistory(List<RankHistoryEntry> leaderboardHistory) {
//        this.leaderboardHistory = leaderboardHistory;
//    }
}
