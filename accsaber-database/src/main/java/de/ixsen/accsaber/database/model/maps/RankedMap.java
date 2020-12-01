package de.ixsen.accsaber.database.model.maps;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class RankedMap {

    @Id
    private Long leaderboardId;
    private int maxScore;

    @ManyToOne(cascade = CascadeType.ALL)
    private Song song;

    private String difficulty;

    private double techyness;

    public Long getLeaderboardId() {
        return this.leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public int getMaxScore() {
        return this.maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public Song getSong() {
        return this.song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public double getTechyness() {
        return this.techyness;
    }

    public void setTechyness(double techyness) {
        this.techyness = techyness;
    }
}
