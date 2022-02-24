package de.ixsen.accsaber.integration.model.scoresaber.score;

public class ScoreSaberScoreDto {
    private long id;
    private int modifiedScore;
    private int baseScore;
    private String modifiers;
    private String timeSet;
    private Hmd hmd;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getModifiedScore() {
        return this.modifiedScore;
    }

    public void setModifiedScore(int modifiedScore) {
        this.modifiedScore = modifiedScore;
    }

    public int getBaseScore() {
        return this.baseScore;
    }

    public void setBaseScore(int baseScore) {
        this.baseScore = baseScore;
    }

    public String getModifiers() {
        return this.modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }

    public String getTimeSet() {
        return this.timeSet;
    }

    public void setTimeSet(String timeSet) {
        this.timeSet = timeSet;
    }

    public Hmd getHmd() {
        return this.hmd;
    }

    public void setHmd(Hmd hmd) {
        this.hmd = hmd;
    }
}
