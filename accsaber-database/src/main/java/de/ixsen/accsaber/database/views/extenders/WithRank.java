package de.ixsen.accsaber.database.views.extenders;

import org.hibernate.annotations.Immutable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Immutable
public abstract class WithRank {
    private Long ranking;

    public Long getRanking() {
        return ranking;
    }
}
