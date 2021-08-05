package de.ixsen.accsaber.database.model.players;

import de.ixsen.accsaber.database.model.PlayerCategoryStats;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;
import java.util.Set;

@Entity
public class PlayerData {

    @Id
    private Long playerId;

    private String playerName;

    private String avatarUrl;

    @OneToMany(mappedBy = "player")
    @OrderBy("timeSet desc")
    private List<ScoreData> scores;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private Set<PlayerCategoryStats> playerCategoryStats;

    @ColumnDefault("false")
    private boolean isAccChamp;

    private String hmd;

    public Long getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(Long playerId) {
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

    public Set<PlayerCategoryStats> getPlayerCategoryStats() {
        return this.playerCategoryStats;
    }

    public void setPlayerCategoryStats(Set<PlayerCategoryStats> playerCategoryStats) {
        this.playerCategoryStats = playerCategoryStats;
    }
}
