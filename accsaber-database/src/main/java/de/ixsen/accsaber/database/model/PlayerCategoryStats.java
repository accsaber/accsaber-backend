package de.ixsen.accsaber.database.model;

import de.ixsen.accsaber.database.model.players.PlayerData;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(PlayerCategoryStatsPK.class)
public class PlayerCategoryStats {

    @ManyToOne
    @JoinColumn(name = "player_id")
    @Id
    private PlayerData player;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Id
    private Category category;

    @ColumnDefault("0")
    private Double averageAcc;

    @ColumnDefault("0")
    private Double ap;

    @ColumnDefault("0")
    private Double averageAp;

    @ColumnDefault("0")
    private int rankedPlays;

    @ColumnDefault("0")
    private int rankingLastWeek;

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

    public int getRankingLastWeek() {
        return this.rankingLastWeek;
    }

    public void setRankingLastWeek(int oneWeekDelta) {
        this.rankingLastWeek = oneWeekDelta;
    }

    public Double getAverageAp() {
        return this.averageAp;
    }

    public void setAverageAp(Double averageAp) {
        this.averageAp = averageAp;
    }
}
