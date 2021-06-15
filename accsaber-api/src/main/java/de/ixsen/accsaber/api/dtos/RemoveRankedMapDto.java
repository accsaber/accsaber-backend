package de.ixsen.accsaber.api.dtos;

public class RemoveRankedMapDto {
    private Long leaderboardId;
    private String beatSaverKey;

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
}
