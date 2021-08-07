package de.ixsen.accsaber.integration.model.beatsaver;

public class BeatSaverInstant {
    private long epochSeconds;

    private int nanosecondsOfSecond;

    private String value;

    public long getEpochSeconds() {
        return epochSeconds;
    }

    public void setEpochSeconds(long epochSeconds) {
        this.epochSeconds = epochSeconds;
    }

    public int getNanosecondsOfSecond() {
        return nanosecondsOfSecond;
    }

    public void setNanosecondsOfSecond(int nanosecondsOfSecond) {
        this.nanosecondsOfSecond = nanosecondsOfSecond;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
