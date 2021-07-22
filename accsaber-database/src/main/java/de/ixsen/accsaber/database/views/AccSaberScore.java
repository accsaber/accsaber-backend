package de.ixsen.accsaber.database.views;


import de.ixsen.accsaber.database.model.players.ScoreData;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;

@Entity
@Immutable
public class AccSaberScore extends ScoreData {

    private Long ranking;

    public Long getRanking() {
        return this.ranking;
    }

    public void setRanking(Long ranking) {
        this.ranking = ranking;
    }
}
