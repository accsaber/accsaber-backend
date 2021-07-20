package de.ixsen.accsaber.database.views;

import de.ixsen.accsaber.database.model.players.PlayerData;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "player")
public class Player extends PlayerData {

    private Long ranking;

    private Double averageAcc;

    private Double ap;
    private Double averageApPerMap;

    private int rankedPlays;

    public Long getRanking() {
        return this.ranking;
    }

    public void setRanking(Long ranking) {
        this.ranking = ranking;
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
}
