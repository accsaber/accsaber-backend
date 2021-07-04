package de.ixsen.accsaber.api.suggested_maps;

public class SuggestedMapDto {
    private String songName;
    private String songSubName;
    private String songAuthorName;
    private String levelAuthorName;

    private String difficulty;

    private Long leaderboardId;
    private String beatSaverKey;
    private String songHash;

    private double complexity;

    private long upVotes;
    private long downVotes;
    private int staffVote;

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

    public String getSongHash() {
        return this.songHash;
    }

    public void setSongHash(String songHash) {
        this.songHash = songHash;
    }

    public double getComplexity() {
        return this.complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }

    public long getUpVotes() {
        return this.upVotes;
    }

    public void setUpVotes(long upVotes) {
        this.upVotes = upVotes;
    }

    public long getDownVotes() {
        return this.downVotes;
    }

    public void setDownVotes(long downVotes) {
        this.downVotes = downVotes;
    }

    public int getStaffVote() {
        return this.staffVote;
    }

    public void setStaffVote(int staffVote) {
        this.staffVote = staffVote;
    }
}
