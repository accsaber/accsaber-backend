package de.ixsen.accsaber.integration.model.scoresaber.score;

public class ScoreSaberDifficulty {
    private Long leaderboardId;
    private int difficulty;

    public Long getLeaderboardId() {
        return leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
