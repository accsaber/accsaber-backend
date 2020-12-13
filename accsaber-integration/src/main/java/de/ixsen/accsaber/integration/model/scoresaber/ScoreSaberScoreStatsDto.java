package de.ixsen.accsaber.integration.model.scoresaber;

public class ScoreSaberScoreStatsDto {
    private int totalScore;

    private int totalRankedScore;

    private double averageRankedAccuracy;

    private int totalPlayCount;

    private int rankedPlayCount;

    public void setTotalScore(int totalScore){
        this.totalScore = totalScore;
    }
    public int getTotalScore(){
        return this.totalScore;
    }
    public void setTotalRankedScore(int totalRankedScore){
        this.totalRankedScore = totalRankedScore;
    }
    public int getTotalRankedScore(){
        return this.totalRankedScore;
    }
    public void setAverageRankedAccuracy(double averageRankedAccuracy){
        this.averageRankedAccuracy = averageRankedAccuracy;
    }
    public double getAverageRankedAccuracy(){
        return this.averageRankedAccuracy;
    }
    public void setTotalPlayCount(int totalPlayCount){
        this.totalPlayCount = totalPlayCount;
    }
    public int getTotalPlayCount(){
        return this.totalPlayCount;
    }
    public void setRankedPlayCount(int rankedPlayCount){
        this.rankedPlayCount = rankedPlayCount;
    }
    public int getRankedPlayCount(){
        return this.rankedPlayCount;
    }
}
