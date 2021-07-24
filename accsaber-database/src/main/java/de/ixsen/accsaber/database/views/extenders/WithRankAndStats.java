package de.ixsen.accsaber.database.views.extenders;

public abstract class WithRankAndStats extends WithRank {

    private Double averageAcc;

    private Double ap;
    private Double averageApPerMap;

    private int rankedPlays;

    public Double getAp() {
        return ap;
    }

    public Double getAverageApPerMap() {
        return averageApPerMap;
    }

    public int getRankedPlays() {
        return rankedPlays;
    }

    public Double getAverageAcc() {
        return averageAcc;
    }
}
