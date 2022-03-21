package de.ixsen.accsaber.integration.model.scoresaber.score;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Hmd {
    UNKNOWN(0, ""),
    CV1(1, ""),
    VIVE(2, ""),
    VIVE_PRO(4, ""),
    WMR(8, ""),
    RIFT_S(16, ""),
    QUEST(32, ""),
    INDEX(64, ""),
    COSMOS(128, "");

    private final int code;
    private final String name;

    Hmd(int code, String name) {
        this.code = code;
        this.name = name;
    }

    @JsonCreator
    public static Hmd fromCode(Integer code) {
        return Arrays.stream(values()).filter(e -> e.code == code)
                .findFirst()
                .orElse(UNKNOWN);
    }
}
