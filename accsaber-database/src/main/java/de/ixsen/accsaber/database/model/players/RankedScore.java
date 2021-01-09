package de.ixsen.accsaber.database.model.players;


import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "ranked_score")
public class RankedScore extends AbstractScore {

    private Long ranking;

    public Long getRanking() {
        return this.ranking;
    }

    public void setRanking(Long ranking) {
        this.ranking = ranking;
    }
}
