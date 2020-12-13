package de.ixsen.accsaber.integration.model.scoresaber;

public class ScoreSaberPlayerInfoDto {
    private String playerId;

    private String playerName;

    private String avatar;

    private int rank;

    private int countryRank;

    private double pp;

    private String country;

    private String role;

    private String history;

    private int permissions;

    private int inactive;

    private int banned;

    public void setPlayerId(String playerId){
        this.playerId = playerId;
    }
    public String getPlayerId(){
        return this.playerId;
    }
    public void setPlayerName(String playerName){
        this.playerName = playerName;
    }
    public String getPlayerName(){
        return this.playerName;
    }
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    public String getAvatar(){
        return this.avatar;
    }
    public void setRank(int rank){
        this.rank = rank;
    }
    public int getRank(){
        return this.rank;
    }
    public void setCountryRank(int countryRank){
        this.countryRank = countryRank;
    }
    public int getCountryRank(){
        return this.countryRank;
    }
    public void setPp(double pp){
        this.pp = pp;
    }
    public double getPp(){
        return this.pp;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return this.country;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getRole(){
        return this.role;
    }
    public void setHistory(String history){
        this.history = history;
    }
    public String getHistory(){
        return this.history;
    }
    public void setPermissions(int permissions){
        this.permissions = permissions;
    }
    public int getPermissions(){
        return this.permissions;
    }
    public void setInactive(int inactive){
        this.inactive = inactive;
    }
    public int getInactive(){
        return this.inactive;
    }
    public void setBanned(int banned){
        this.banned = banned;
    }
    public int getBanned(){
        return this.banned;
    }
}
