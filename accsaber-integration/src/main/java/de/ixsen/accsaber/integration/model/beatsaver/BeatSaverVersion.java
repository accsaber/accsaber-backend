package de.ixsen.accsaber.integration.model.beatsaver;

import java.util.List;

public class BeatSaverVersion {
    private String coverURL;

    private String createdAt;

    private List<BeatSaverMapDifficulty> diffs;

    private String downloadURL;

    private String feedback;

    private String hash;

    private String key;

    private String previewURL;

    private int sageScore;

    private String state;

    private String testplayAt;

    private List<BeatSaverTestplay> testplays;

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<BeatSaverMapDifficulty> getDiffs() {
        return diffs;
    }

    public void setDiffs(List<BeatSaverMapDifficulty> diffs) {
        this.diffs = diffs;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPreviewURL() {
        return previewURL;
    }

    public void setPreviewURL(String previewURL) {
        this.previewURL = previewURL;
    }

    public int getSageScore() {
        return sageScore;
    }

    public void setSageScore(int sageScore) {
        this.sageScore = sageScore;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTestplayAt() {
        return testplayAt;
    }

    public void setTestplayAt(String testplayAt) {
        this.testplayAt = testplayAt;
    }

    public List<BeatSaverTestplay> getTestplays() {
        return testplays;
    }

    public void setTestplays(List<BeatSaverTestplay> testplays) {
        this.testplays = testplays;
    }
}
