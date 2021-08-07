package de.ixsen.accsaber.integration.model.beatsaver;

public class BeatSaverUserStats {

    private double avgBpm;

    private double avgDuration;

    private double avgScore;

    private BeatSaverUserDiffStats diffStats;

    private String firstUpload;

    private String lastUpload;

    private int rankedMaps;

    private int totalDownvotes;

    private int totalMaps;

    private int totalUpvotes;

    public double getAvgBpm() {
        return avgBpm;
    }

    public void setAvgBpm(double avgBpm) {
        this.avgBpm = avgBpm;
    }

    public double getAvgDuration() {
        return avgDuration;
    }

    public void setAvgDuration(double avgDuration) {
        this.avgDuration = avgDuration;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public void setAvgScore(double avgScore) {
        this.avgScore = avgScore;
    }

    public BeatSaverUserDiffStats getDiffStats() {
        return diffStats;
    }

    public void setDiffStats(BeatSaverUserDiffStats diffStats) {
        this.diffStats = diffStats;
    }

    public String getFirstUpload() {
        return firstUpload;
    }

    public void setFirstUpload(String firstUpload) {
        this.firstUpload = firstUpload;
    }

    public String getLastUpload() {
        return lastUpload;
    }

    public void setLastUpload(String lastUpload) {
        this.lastUpload = lastUpload;
    }

    public int getRankedMaps() {
        return rankedMaps;
    }

    public void setRankedMaps(int rankedMaps) {
        this.rankedMaps = rankedMaps;
    }

    public int getTotalDownvotes() {
        return totalDownvotes;
    }

    public void setTotalDownvotes(int totalDownvotes) {
        this.totalDownvotes = totalDownvotes;
    }

    public int getTotalMaps() {
        return totalMaps;
    }

    public void setTotalMaps(int totalMaps) {
        this.totalMaps = totalMaps;
    }

    public int getTotalUpvotes() {
        return totalUpvotes;
    }

    public void setTotalUpvotes(int totalUpvotes) {
        this.totalUpvotes = totalUpvotes;
    }
}
