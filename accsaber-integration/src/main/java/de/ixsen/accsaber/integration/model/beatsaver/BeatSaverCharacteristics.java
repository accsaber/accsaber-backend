package de.ixsen.accsaber.integration.model.beatsaver;

import java.util.Map;

public class BeatSaverCharacteristics {
    private Map<String, BeatSaverDifficultyDetails> difficulties;

    private String name;

    public void setDifficulties(Map<String, BeatSaverDifficultyDetails> difficulties) {
        this.difficulties = difficulties;
    }

    public Map<String, BeatSaverDifficultyDetails> getDifficulties() {
        return this.difficulties;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
