package de.ixsen.accsaber.database.model.players;

import de.ixsen.accsaber.database.model.CategoryPerformance;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class Player {

    @Id
    private String playerId;

    private String playerName;

    private String avatarUrl;

    @OneToMany
    @OrderBy("timeSet desc")
    private List<Score> scores;

    @OneToMany
    private Set<CategoryPerformance> categoryPerformances;

    @Column(insertable = false, updatable = false)
    @Transient
    private Long rank;

    @Column(insertable = false, updatable = false)
    @Transient
    private Double averageAcc;

    @Column(insertable = false, updatable = false)
    @Transient
    private Double ap;

    @Column(insertable = false, updatable = false)
    @Transient
    private Double averageApPerMap;

    @ColumnDefault("0")
    @Transient
    private int rankedPlays;

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

    public Long getRank() {
        return this.rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public List<Score> getScores() {
        return this.scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public void addScore(Score score) {
        this.getScores().add(score);
        score.setPlayer(this);
    }

    public Set<CategoryPerformance> getCategoryPerformances() {
        return this.categoryPerformances;
    }

    public void setCategoryPerformances(Set<CategoryPerformance> categoryPerformances) {
        this.categoryPerformances = categoryPerformances;
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
}
