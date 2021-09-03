package de.ixsen.accsaber.database.model.players;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PlayerRankHistoryPK implements Serializable {

    protected Long player;
    protected LocalDate date;
    protected Long category;

    public PlayerRankHistoryPK() {
    }

    public PlayerRankHistoryPK(Long player, LocalDate date) {
        this.player = player;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        PlayerRankHistoryPK that = (PlayerRankHistoryPK) o;
        return this.player.equals(that.player) && this.date.equals(that.date) && this.category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.player, this.date, this.category);
    }
}
