package de.ixsen.accsaber.database.views;

import de.ixsen.accsaber.database.model.PlayerCategoryStats;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Immutable
@Table(name = "player_overall_stats")
public class PlayerOverallStats extends PlayerCategoryStats {

    private Long ranking;

    public Long getRanking() {
        return this.ranking;
    }

    public void setRanking(Long ranking) {
        this.ranking = ranking;
    }
}
