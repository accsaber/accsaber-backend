package de.ixsen.accsaber.integration.model.beatsaver;

public class BeatSaverDifficulty {
    private BeatSaverDifficultyDetails easy;

    private BeatSaverDifficultyDetails expert;

    private BeatSaverDifficultyDetails expertPlus;

    private BeatSaverDifficultyDetails hard;

    private BeatSaverDifficultyDetails normal;

    public void setEasy(BeatSaverDifficultyDetails easy) {
        this.easy = easy;
    }

    public BeatSaverDifficultyDetails getEasy() {
        return this.easy;
    }

    public void setExpert(BeatSaverDifficultyDetails expert) {
        this.expert = expert;
    }

    public BeatSaverDifficultyDetails getExpert() {
        return this.expert;
    }

    public void setExpertPlus(BeatSaverDifficultyDetails expertPlus) {
        this.expertPlus = expertPlus;
    }

    public BeatSaverDifficultyDetails getExpertPlus() {
        return this.expertPlus;
    }

    public void setHard(BeatSaverDifficultyDetails hard) {
        this.hard = hard;
    }

    public BeatSaverDifficultyDetails getHard() {
        return this.hard;
    }

    public void setNormal(BeatSaverDifficultyDetails normal) {
        this.normal = normal;
    }

    public BeatSaverDifficultyDetails getNormal() {
        return this.normal;
    }
}
