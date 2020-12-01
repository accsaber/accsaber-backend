package de.ixsen.accsaber.integration.model.scoresaber;

import java.util.ArrayList;

public class ScoreSaberScoreListDto {
    private ArrayList<ScoreSaberScoreDto> scores;

    public ArrayList<ScoreSaberScoreDto> getScores() {
        return this.scores;
    }

    public void setScores(ArrayList<ScoreSaberScoreDto> scores) {
        this.scores = scores;
    }
}
