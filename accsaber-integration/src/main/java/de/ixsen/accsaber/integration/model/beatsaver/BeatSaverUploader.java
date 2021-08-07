package de.ixsen.accsaber.integration.model.beatsaver;

public class BeatSaverUploader {
    private String avatar;

    private String hash;

    private int id;

    private String name;

    private BeatSaverUserStats stats;

    private boolean testplay;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BeatSaverUserStats getStats() {
        return stats;
    }

    public void setStats(BeatSaverUserStats stats) {
        this.stats = stats;
    }

    public boolean isTestplay() {
        return testplay;
    }

    public void setTestplay(boolean testplay) {
        this.testplay = testplay;
    }
}
