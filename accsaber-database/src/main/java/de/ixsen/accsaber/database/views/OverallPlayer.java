package de.ixsen.accsaber.database.views;

import de.ixsen.accsaber.database.model.players.PlayerData;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;

@Entity
@Immutable
public class OverallPlayer extends PlayerData {

    private Long ranking;

    private Double averageAcc;

    private Double ap;
    private Double averageApPerMap;

    private int rankedPlays;

    public Long getRanking() {
        return this.ranking;
    }

    private void setRanking(Long ranking) {
        this.ranking = ranking;
    }

    public Double getAverageAcc() {
        return this.averageAcc;
    }

    private void setAverageAcc(Double averageAcc) {
        this.averageAcc = averageAcc;
    }

    public Double getAp() {
        return this.ap;
    }

    private void setAp(Double ap) {
        this.ap = ap;
    }

    public Double getAverageApPerMap() {
        return this.averageApPerMap;
    }

    private void setAverageApPerMap(Double averageApPerMap) {
        this.averageApPerMap = averageApPerMap;
    }

    public int getRankedPlays() {
        return this.rankedPlays;
    }

    private void setRankedPlays(int rankedPlays) {
        this.rankedPlays = rankedPlays;
    }
}
