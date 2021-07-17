package de.ixsen.accsaber.database.model.players;

import de.ixsen.accsaber.database.model.CategoryPerformance;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "player")
public class Player extends AbstractPlayer {

    @OneToMany
    @OrderBy("timeSet desc")
    private List<Score> scores;

    @OneToMany
    private Set<CategoryPerformance> categoryPerformances;

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

    public void setLeaderboardPerformances(Set<CategoryPerformance> categoryPerformances) {
        this.categoryPerformances = categoryPerformances;
    }
}
