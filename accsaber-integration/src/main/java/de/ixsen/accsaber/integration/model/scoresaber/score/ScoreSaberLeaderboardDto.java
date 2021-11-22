package de.ixsen.accsaber.integration.model.scoresaber.score;

public class ScoreSaberLeaderboardDto {
    private long id;
    private String songHash;
    private String songName;
    private String songSubName;
    private String songAuthorName;
    private String levelAuthorName;
    private int difficulty;
    private String difficultyRaw;
    private int maxScore;
    private String coverImage;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSongHash() {
        return this.songHash;
    }

    public void setSongHash(String songHash) {
        this.songHash = songHash;
    }

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

    public int getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getDifficultyRaw() {
        return this.difficultyRaw;
    }

    public void setDifficultyRaw(String difficultyRaw) {
        this.difficultyRaw = difficultyRaw;
    }

    public int getMaxScore() {
        return this.maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getCoverImage() {
        return this.coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
