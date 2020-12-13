package de.ixsen.accsaber.integration.model.beatsaver;

public class BeatSaverStats {
    private int downloads;

    private int plays;

    private int downVotes;

    private int upVotes;

    private double heat;

    private double rating;

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public int getDownloads() {
        return this.downloads;
    }

    public void setPlays(int plays) {
        this.plays = plays;
    }

    public int getPlays() {
        return this.plays;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public int getDownVotes() {
        return this.downVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public int getUpVotes() {
        return this.upVotes;
    }

    public void setHeat(double heat) {
        this.heat = heat;
    }

    public double getHeat() {
        return this.heat;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return this.rating;
    }
}
