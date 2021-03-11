package de.ixsen.accsaber.database.model.players;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.Map;

@MappedSuperclass
public abstract class AbstractPlayer {

    @Id
    private String playerId;

    private String playerName;
    private String avatarUrl;

    @Column(name = "is_acc_champ")
    private Boolean isAccChamp;
    private String hmd;

    private Double averageAcc;

    private Double ap;
    private Double averageApPerMap;
    private int rankedPlays;

//    @ElementCollection(fetch = FetchType.EAGER)
//    private Map<Date, Integer> rankHistory;


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

    public Boolean isAccChamp() {
        return this.isAccChamp;
    }

    public void setAccChamp(Boolean accChamp) {
        this.isAccChamp = accChamp;
    }

    public String getHmd() {
        return this.hmd;
    }

    public void setHmd(String hmd) {
        this.hmd = hmd;
    }

    public Double getAverageAcc() {
        return this.averageAcc;
    }

    public void setAverageAcc(Double averageAcc) {
        this.averageAcc = averageAcc;
    }

    public Double getAp() {
        return this.ap;
    }

    public void setAp(Double ap) {
        this.ap = ap;
    }

    public Double getAverageApPerMap() {
        return this.averageApPerMap;
    }

    public void setAverageApPerMap(Double averageApPerMap) {
        this.averageApPerMap = averageApPerMap;
    }

    public int getRankedPlays() {
        return this.rankedPlays;
    }

    public void setRankedPlays(int rankedPlays) {
        this.rankedPlays = rankedPlays;
    }

//    public Map<Date, Integer> getRankHistory() {
//        return this.rankHistory;
//    }
//
//    public void setRankHistory(Map<Date, Integer> rankHistory) {
//        this.rankHistory = rankHistory;
//    }
}
