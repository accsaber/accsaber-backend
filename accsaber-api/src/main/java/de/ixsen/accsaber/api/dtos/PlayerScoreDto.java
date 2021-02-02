package de.ixsen.accsaber.api.dtos;

public class PlayerScoreDto {
    private long rank;
    private double ap;
    private long score;
    private double accuracy;

    private String songName;
    private String songAuthorName;
    private String levelAuthorName;
    private double techyness;
    private String songHash;

    private String difficulty;
    private String leaderboardId;
    private String beatsaverKey;

    private String timeSet;

    public long getRank() {
        return this.rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public double getAp() {
        return this.ap;
    }

    public void setAp(double ap) {
        this.ap = ap;
    }

    public long getScore() {
        return this.score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getSongName() {
        return this.songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
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

    public double getTechyness() {
        return this.techyness;
    }

    public void setTechyness(double techyness) {
        this.techyness = techyness;
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

    public String getTimeSet() {
        return this.timeSet;
    }

    public void setTimeSet(String timeSet) {
        this.timeSet = timeSet;
    }

    public String getSongHash() {
        return songHash;
    }

    public void setSongHash(String songHash) {
        this.songHash = songHash;
    }
}
