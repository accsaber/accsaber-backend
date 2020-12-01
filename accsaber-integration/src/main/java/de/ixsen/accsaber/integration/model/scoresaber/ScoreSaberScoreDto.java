package de.ixsen.accsaber.integration.model.scoresaber;

public class ScoreSaberScoreDto {
    private int rank;
    private int scoreId;
    private int score;
    private int unmodififiedScore;
    private String mods;
    private int pp;
    private int weight;
    private String timeSet;
    private int leaderboardId;
    private String songHash;
    private String songName;
    private String songSubName;
    private String songAuthorName;
    private String levelAuthorName;
    private int difficulty;
    private String difficultyRaw;
    private int maxScore;

    public void setRank(int rank){
        this.rank = rank;
    }
    public int getRank(){
        return this.rank;
    }
    public void setScoreId(int scoreId){
        this.scoreId = scoreId;
    }
    public int getScoreId(){
        return this.scoreId;
    }
    public void setScore(int score){
        this.score = score;
    }
    public int getScore(){
        return this.score;
    }
    public void setUnmodififiedScore(int unmodififiedScore){
        this.unmodififiedScore = unmodififiedScore;
    }
    public int getUnmodififiedScore(){
        return this.unmodififiedScore;
    }
    public void setMods(String mods){
        this.mods = mods;
    }
    public String getMods(){
        return this.mods;
    }
    public void setPp(int pp){
        this.pp = pp;
    }
    public int getPp(){
        return this.pp;
    }
    public void setWeight(int weight){
        this.weight = weight;
    }
    public int getWeight(){
        return this.weight;
    }
    public void setTimeSet(String timeSet){
        this.timeSet = timeSet;
    }
    public String getTimeSet(){
        return this.timeSet;
    }
    public void setLeaderboardId(int leaderboardId){
        this.leaderboardId = leaderboardId;
    }
    public int getLeaderboardId(){
        return this.leaderboardId;
    }
    public void setSongHash(String songHash){
        this.songHash = songHash;
    }
    public String getSongHash(){
        return this.songHash;
    }
    public void setSongName(String songName){
        this.songName = songName;
    }
    public String getSongName(){
        return this.songName;
    }
    public void setSongSubName(String songSubName){
        this.songSubName = songSubName;
    }
    public String getSongSubName(){
        return this.songSubName;
    }
    public void setSongAuthorName(String songAuthorName){
        this.songAuthorName = songAuthorName;
    }
    public String getSongAuthorName(){
        return this.songAuthorName;
    }
    public void setLevelAuthorName(String levelAuthorName){
        this.levelAuthorName = levelAuthorName;
    }
    public String getLevelAuthorName(){
        return this.levelAuthorName;
    }
    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }
    public int getDifficulty(){
        return this.difficulty;
    }
    public void setDifficultyRaw(String difficultyRaw){
        this.difficultyRaw = difficultyRaw;
    }
    public String getDifficultyRaw(){
        return this.difficultyRaw;
    }
    public void setMaxScore(int maxScore){
        this.maxScore = maxScore;
    }
    public int getMaxScore(){
        return this.maxScore;
    }
}
