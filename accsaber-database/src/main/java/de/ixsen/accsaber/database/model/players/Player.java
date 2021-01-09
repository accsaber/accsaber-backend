package de.ixsen.accsaber.database.model.players;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "player")
public class Player extends AbstractPlayer {

    @OneToMany
    @OrderBy("timeSet desc")
    private List<Score> scores;

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

}
