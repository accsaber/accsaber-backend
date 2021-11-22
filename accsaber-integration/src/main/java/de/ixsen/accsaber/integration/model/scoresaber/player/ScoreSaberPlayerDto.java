package de.ixsen.accsaber.integration.model.scoresaber.player;

public class ScoreSaberPlayerDto {
    private String id;
    private String Name;
    private String profilePicture;
    private String country;

    private ScoreSaberScoreStatsDto scoreStats;

    public void setScoreStats(ScoreSaberScoreStatsDto scoreStats) {
        this.scoreStats = scoreStats;
    }

    public ScoreSaberScoreStatsDto getScoreStats() {
        return this.scoreStats;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getProfilePicture() {
        return this.profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
