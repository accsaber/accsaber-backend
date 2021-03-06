package de.ixsen.accsaber.database.model.players;

import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "ranked_player")
public class RankedPlayer extends AbstractPlayer {

    private Long ranking;

    public Long getRanking() {
        return this.ranking;
    }

    public void setRanking(Long ranking) {
        this.ranking = ranking;
    }

}
