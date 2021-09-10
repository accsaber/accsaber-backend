package de.ixsen.accsaber.database.model.players;

import de.ixsen.accsaber.database.model.Category;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
@IdClass(PlayerRankHistoryPK.class)
public class PlayerRankHistory {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false, name = "player_id", foreignKey = @ForeignKey(name = "none"))
    @Id
    private PlayerData player;

    @Id
    private LocalDate date;

    private int ranking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(insertable = false, updatable = false, name = "category_id", foreignKey = @ForeignKey(name = "none"))
    @Id
    private Category category;

    private double ap;

    private double averageAcc;

    private double averageApPerMap;

    private int rankedPlays;

    public PlayerData getPlayer() {
        return this.player;
    }

    public void setPlayer(PlayerData player) {
        this.player = player;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRanking() {
        return this.ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getAp() {
        return this.ap;
    }

    public void setAp(double ap) {
        this.ap = ap;
    }

    public double getAverageAcc() {
        return this.averageAcc;
    }

    public void setAverageAcc(double averageAcc) {
        this.averageAcc = averageAcc;
    }

    public double getAverageApPerMap() {
        return this.averageApPerMap;
    }

    public void setAverageApPerMap(double averageApPerMap) {
        this.averageApPerMap = averageApPerMap;
    }

    public int getRankedPlays() {
        return this.rankedPlays;
    }

    public void setRankedPlays(int rankedPlays) {
        this.rankedPlays = rankedPlays;
    }
}
