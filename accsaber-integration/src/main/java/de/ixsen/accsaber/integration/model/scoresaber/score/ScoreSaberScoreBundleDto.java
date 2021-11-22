package de.ixsen.accsaber.integration.model.scoresaber.score;

public class ScoreSaberScoreBundleDto {
    private ScoreSaberScoreDto score;
    private ScoreSaberLeaderboardDto leaderboard;

    public ScoreSaberScoreDto getScore() {
        return this.score;
    }

    public void setScore(ScoreSaberScoreDto score) {
        this.score = score;
    }

    public ScoreSaberLeaderboardDto getLeaderboard() {
        return this.leaderboard;
    }

    public void setLeaderboard(ScoreSaberLeaderboardDto leaderboard) {
        this.leaderboard = leaderboard;
    }
}
