package de.ixsen.accsaber.api.dtos;

public class CreateRankedMapDto {
    private Long leaderboardId;
    private String beatSaverKey;
    private String difficulty;
    private double complexity;

    public Long getLeaderboardId() {
        return this.leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public String getBeatSaverKey() {
        return this.beatSaverKey;
    }

    public void setBeatSaverKey(String beatSaverKey) {
        this.beatSaverKey = beatSaverKey;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public double getComplexity() {
        return this.complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }
}
