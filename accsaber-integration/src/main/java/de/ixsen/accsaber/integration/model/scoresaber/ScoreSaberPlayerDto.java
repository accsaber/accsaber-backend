package de.ixsen.accsaber.integration.model.scoresaber;

public class ScoreSaberPlayerDto {
    private ScoreSaberPlayerInfoDto playerInfo;

    private ScoreSaberScoreStatsDto scoreStats;

    public void setPlayerInfo(ScoreSaberPlayerInfoDto playerInfo){
        this.playerInfo = playerInfo;
    }
    public ScoreSaberPlayerInfoDto getPlayerInfo(){
        return this.playerInfo;
    }
    public void setScoreStats(ScoreSaberScoreStatsDto scoreStats){
        this.scoreStats = scoreStats;
    }
    public ScoreSaberScoreStatsDto getScoreStats(){
        return this.scoreStats;
    }
}
