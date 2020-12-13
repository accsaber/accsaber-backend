package de.ixsen.accsaber.api.dtos;

public class CreateRankedMapDto {
    private Long leaderBoardId;
    private String beatSaverId;
    private String difficulty;
    private double techyness;

    public Long getLeaderBoardId() {
        return this.leaderBoardId;
    }

    public void setLeaderBoardId(Long leaderBoardId) {
        this.leaderBoardId = leaderBoardId;
    }

    public String getBeatSaverId() {
        return this.beatSaverId;
    }

    public void setBeatSaverId(String beatSaverId) {
        this.beatSaverId = beatSaverId;
    }

    public String getDifficulty() {
        return this.difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public double getTechyness() {
        return this.techyness;
    }

    public void setTechyness(double techyness) {
        this.techyness = techyness;
    }
}
