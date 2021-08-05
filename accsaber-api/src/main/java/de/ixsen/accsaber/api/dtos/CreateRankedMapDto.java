package de.ixsen.accsaber.api.dtos;

public class CreateRankedMapDto {
    private Long leaderboardId;
    private String songHash;
    private String difficulty;
    private double complexity;
    private String categoryName;

    public Long getLeaderboardId() {
        return this.leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public String getSongHash() {
        return this.songHash;
    }

    public void setSongHash(String songHash) {
        this.songHash = songHash;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getComplexity() {
        return this.complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }
}
