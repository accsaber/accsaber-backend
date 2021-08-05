package de.ixsen.accsaber.integration.model.beatsaver;

public class BeatSaverMetadata {
    private double bpm;

    private int duration;

    private String levelAuthorName;

    private String songAuthorName;

    private String songName;

    private String songSubName;

    public double getBpm() {
        return bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLevelAuthorName() {
        return levelAuthorName;
    }

    public void setLevelAuthorName(String levelAuthorName) {
        this.levelAuthorName = levelAuthorName;
    }

    public String getSongAuthorName() {
        return songAuthorName;
    }

    public void setSongAuthorName(String songAuthorName) {
        this.songAuthorName = songAuthorName;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongSubName() {
        return songSubName;
    }

    public void setSongSubName(String songSubName) {
        this.songSubName = songSubName;
    }
}
