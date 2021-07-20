package de.ixsen.accsaber.database.model;

import de.ixsen.accsaber.database.model.players.PlayerData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"player", "date"}))
public class RankHistoryEntry extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "player")
    private PlayerData player;

    @Column(name = "date")
    private Date date;

    public PlayerData getPlayer() {
        return this.player;
    }

    public void setPlayer(PlayerData player) {
        this.player = player;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
