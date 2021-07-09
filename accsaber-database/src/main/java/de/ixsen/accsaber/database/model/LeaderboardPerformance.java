package de.ixsen.accsaber.database.model;

import de.ixsen.accsaber.database.model.players.Player;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
public class LeaderboardPerformance extends BaseEntity {
    @ManyToOne
    private Player player;
    @ManyToOne
    private Leaderboard leaderboard;


    private Double averageAcc;

    private Double ap;
    private Double averageApPerMap;
    private int rankedPlays;

    @ManyToMany
    private List<RankHistoryEntry> leaderboardHistory;
}
