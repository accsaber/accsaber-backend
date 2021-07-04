package de.ixsen.accsaber.api.suggested_maps;

public class MapSuggestionDto {

    private Long leaderboardId;
    private String beatSaverKey;
    private String difficulty;

    private double complexity;
    private String type;

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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
