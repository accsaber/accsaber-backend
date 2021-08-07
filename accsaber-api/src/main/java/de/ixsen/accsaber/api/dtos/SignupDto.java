package de.ixsen.accsaber.api.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignupDto {
    private String scoresaberLink;
    private String playerName;
    private String hmd;

    public String getScoresaberLink() {
        return this.scoresaberLink;
    }

    public void setScoresaberLink(String scoresaberLink) {
        this.scoresaberLink = scoresaberLink;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(final String playerName) {
        this.playerName = playerName;
    }

    public String getHmd() {
        return this.hmd;
    }

    public void setHmd(final String hmd) {
        this.hmd = hmd;
    }
}
