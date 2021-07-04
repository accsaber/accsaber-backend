package de.ixsen.accsaber.api.players;

public class PlayerDto {

    private int rank;

    private String playerId;

    private String playerName;
    private String avatarUrl;

    private boolean isAccChamp;
    private String hmd;

    private double averageAcc;
    private double ap;

    private int rankedPlays;
    private double averageApPerMap;

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public boolean isAccChamp() {
        return this.isAccChamp;
    }

    public void setAccChamp(boolean accChamp) {
        this.isAccChamp = accChamp;
    }

    public String getHmd() {
        return this.hmd;
    }

    public void setHmd(String hmd) {
        this.hmd = hmd;
    }

    public double getAverageAcc() {
        return this.averageAcc;
    }

    public void setAverageAcc(double averageAcc) {
        this.averageAcc = averageAcc;
    }

    public double getAp() {
        return this.ap;
    }

    public void setAp(double ap) {
        this.ap = ap;
    }

    public int getRankedPlays() {
        return this.rankedPlays;
    }

    public void setRankedPlays(int rankedPlays) {
        this.rankedPlays = rankedPlays;
    }

    public double getAverageApPerMap() {
        return this.averageApPerMap;
    }

    public void setAverageApPerMap(double averageApPerMap) {
        this.averageApPerMap = averageApPerMap;
    }
}
