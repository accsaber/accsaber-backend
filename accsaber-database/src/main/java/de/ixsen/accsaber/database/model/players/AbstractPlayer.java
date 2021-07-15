package de.ixsen.accsaber.database.model.players;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractPlayer implements Serializable {

    @Id
    private String playerId;

    private String playerName;
    private String avatarUrl;

    @Column(name = "is_acc_champ")
    private Boolean isAccChamp;
    private String hmd;

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
}
