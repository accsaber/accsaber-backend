package de.ixsen.accsaber.api.ranked_maps;

import java.util.Map;

public class RankedMapsStatisticsDto {
    private int mapCount;
    private long trueAccMapCount;
    private long standardAccMapCount;
    private long techAccMapCount;

    private Map<Double, Long> complexityToMapCount;

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

    public Map<Double, Long> getComplexityToMapCount() {
        return this.complexityToMapCount;
    }

    public void setComplexityToMapCount(Map<Double, Long> complexityToMapCount) {
        this.complexityToMapCount = complexityToMapCount;
    }
}
