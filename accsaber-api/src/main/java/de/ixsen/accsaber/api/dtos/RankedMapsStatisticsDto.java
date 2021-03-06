package de.ixsen.accsaber.api.dtos;

import java.util.Map;

public class RankedMapsStatisticsDto {
    private int mapCount;
    private long trueAccMapCount;
    private long standardAccMapCount;
    private long techAccMapCount;

    private Map<Double, Long> techynessToMapCount;

    public int getMapCount() {
        return this.mapCount;
    }

    public void setMapCount(int mapCount) {
        this.mapCount = mapCount;
    }

    public long getTrueAccMapCount() {
        return this.trueAccMapCount;
    }

    public void setTrueAccMapCount(long trueAccMapCount) {
        this.trueAccMapCount = trueAccMapCount;
    }

    public long getStandardAccMapCount() {
        return this.standardAccMapCount;
    }

    public void setStandardAccMapCount(long standardAccMapCount) {
        this.standardAccMapCount = standardAccMapCount;
    }

    public long getTechAccMapCount() {
        return this.techAccMapCount;
    }

    public void setTechAccMapCount(long techAccMapCount) {
        this.techAccMapCount = techAccMapCount;
    }

    public Map<Double, Long> getTechynessToMapCount() {
        return this.techynessToMapCount;
    }

    public void setTechynessToMapCount(Map<Double, Long> techynessToMapCount) {
        this.techynessToMapCount = techynessToMapCount;
    }
}
