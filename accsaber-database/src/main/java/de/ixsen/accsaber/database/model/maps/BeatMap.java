package de.ixsen.accsaber.database.model.maps;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class BeatMap {

    @Id
    private Long leaderboardId;
    private int maxScore;

    @ManyToOne
    private Song song;

    private String difficulty;

    private double complexity;

    @Enumerated(EnumType.STRING)
    private RankedStage rankedStage;

    @Enumerated(EnumType.STRING)
    private MapType mapType;

    @OneToMany
    private List<MapSuggestionVote> mapSuggestionVotes;

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

    public RankedStage getRankedStage() {
        return this.rankedStage;
    }

    public void setRankedStage(RankedStage rankedStage) {
        this.rankedStage = rankedStage;
    }

    public MapType getMapType() {
        return this.mapType;
    }

    public void setMapType(MapType mapType) {
        this.mapType = mapType;
    }

    public List<MapSuggestionVote> getMapSuggestionVotes() {
        return this.mapSuggestionVotes;
    }

    public void setMapSuggestionVotes(List<MapSuggestionVote> mapSuggestionVotes) {
        this.mapSuggestionVotes = mapSuggestionVotes;
    }
}
