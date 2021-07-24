package de.ixsen.accsaber.database.model.players;

import de.ixsen.accsaber.database.model.PlayerCategoryStats;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
public class PlayerData implements Serializable {

    @Id
    private String playerId;

    private String playerName;

    private String avatarUrl;

    @OneToMany
    @OrderBy("timeSet desc")
    private List<ScoreData> scores;

    @OneToMany(mappedBy = "player")
    private Set<PlayerCategoryStats> playerCategoryStats;

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

    public List<ScoreData> getScores() {
        return this.scores;
    }

    public void setScores(List<ScoreData> scores) {
        this.scores = scores;
    }

    public void addScore(ScoreData score) {
        this.getScores().add(score);
        score.setPlayer(this);
    }

    public Set<PlayerCategoryStats> getCategoryPerformances() {
        return this.playerCategoryStats;
    }

    public void setCategoryPerformances(Set<PlayerCategoryStats> playerCategoryStats) {
        this.playerCategoryStats = playerCategoryStats;
    }
}
