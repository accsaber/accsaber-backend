package de.ixsen.accsaber.integration.model.beatsaver;

import java.util.List;

public class BeatSaverMetadata {
    private BeatSaverExistingDifficulties difficulties;

    private int duration;

    private String automapper;

    private List<BeatSaverCharacteristics> characteristics;

    private String levelAuthorName;

    private String songAuthorName;

    private String songName;

    private String songSubName;

    private int bpm;

    public void setDifficulties(BeatSaverExistingDifficulties difficulties) {
        this.difficulties = difficulties;
    }

    public BeatSaverExistingDifficulties getDifficulties() {
        return this.difficulties;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setAutomapper(String automapper) {
        this.automapper = automapper;
    }

    public String getAutomapper() {
        return this.automapper;
    }

    public void setCharacteristics(List<BeatSaverCharacteristics> characteristics) {
        this.characteristics = characteristics;
    }

    public List<BeatSaverCharacteristics> getCharacteristics() {
        return this.characteristics;
    }

    public void setLevelAuthorName(String levelAuthorName) {
        this.levelAuthorName = levelAuthorName;
    }

    public String getLevelAuthorName() {
        return this.levelAuthorName;
    }

    public void setSongAuthorName(String songAuthorName) {
        this.songAuthorName = songAuthorName;
    }

    public String getSongAuthorName() {
        return this.songAuthorName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongName() {
        return this.songName;
    }

    public void setSongSubName(String songSubName) {
        this.songSubName = songSubName;
    }

    public String getSongSubName() {
        return this.songSubName;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public int getBpm() {
        return this.bpm;
    }
}



