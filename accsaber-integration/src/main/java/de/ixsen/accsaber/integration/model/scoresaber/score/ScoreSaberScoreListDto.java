package de.ixsen.accsaber.integration.model.scoresaber.score;

import java.util.ArrayList;

public class ScoreSaberScoreListDto {
    private ArrayList<ScoreSaberScoreBundleDto> playerScores;

    public ArrayList<ScoreSaberScoreBundleDto> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(ArrayList<ScoreSaberScoreBundleDto> playerScores) {
        this.playerScores = playerScores;
    }
}
