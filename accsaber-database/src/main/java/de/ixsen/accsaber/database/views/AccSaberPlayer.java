package de.ixsen.accsaber.database.views;

import de.ixsen.accsaber.database.model.players.ScoreData;
import de.ixsen.accsaber.database.views.extenders.WithRankAndStats;
import org.hibernate.annotations.Immutable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import java.util.List;

@MappedSuperclass
@Immutable
public abstract class AccSaberPlayer extends WithRankAndStats {

    @Id
    private String playerId;

    private String playerName;

    private String avatarUrl;

    @OneToMany(mappedBy = "")
    @OrderBy("timeSet desc")
    private List<ScoreData> scores;

    private Boolean isAccChamp;

    private String hmd;

    public String getPlayerId() {
        return this.playerId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public List<ScoreData> getScores() {
        return this.scores;
    }

    public Boolean getAccChamp() {
        return this.isAccChamp;
    }

    public String getHmd() {
        return this.hmd;
    }
}
