package de.ixsen.accsaber.api.dtos;

public class RankedMapDto {

    private String songName;
    private String songSubName;
    private String songAuthorName;
    private String levelAuthorName;

    private String difficulty;

    private String leaderboardId;
    private String beatsaverKey;
    private String songHash;

    private double complexity;

    public String getSongName() {
        return this.songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongSubName() {
        return this.songSubName;
    }

    public void setSongSubName(String songSubName) {
        this.songSubName = songSubName;
    }

    public String getSongAuthorName() {
        return this.songAuthorName;
    }

    public void setSongAuthorName(String songAuthorName) {
        this.songAuthorName = songAuthorName;
    }

    public String getLevelAuthorName() {
        return this.levelAuthorName;
    }

    public void setLevelAuthorName(String levelAuthorName) {
        this.levelAuthorName = levelAuthorName;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getLeaderboardId() {
        return this.leaderboardId;
    }

    public void setLeaderboardId(String leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public String getBeatsaverKey() {
        return this.beatsaverKey;
    }

    public void setBeatsaverKey(String beatsaverKey) {
        this.beatsaverKey = beatsaverKey;
    }

    public String getSongHash() {
        return this.songHash;
    }

    public void setSongHash(final String songHash) {
        this.songHash = songHash;
    }

    public double getcomplexity() {
        return this.complexity;
    }

    public void setcomplexity(double complexity) {
        this.complexity = complexity;
    }
}
