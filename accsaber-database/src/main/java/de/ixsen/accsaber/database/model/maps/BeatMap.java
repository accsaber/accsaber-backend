package de.ixsen.accsaber.database.model.maps;

import de.ixsen.accsaber.database.model.Category;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import java.time.Instant;

@Entity
public class BeatMap {

    /**
     * Scoresaber LeaderboardID
     */
    @Id
    private Long leaderboardId;
    private int maxScore;

    @ManyToOne
    @JoinColumn(name = "song")
    private Song song;

    private String difficulty;

    private double complexity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OrderBy
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant dateRanked = Instant.now();

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

    public double getComplexity() {
        return this.complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Instant getDateRanked() {
        return this.dateRanked;
    }

    public void setDateRanked(Instant dateRanked) {
        this.dateRanked = dateRanked;
    }
}
