package de.ixsen.accsaber.integration.model.scoresaber.score

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.*

enum class Hmd(
    private val code: Int,
    private val hmdName: String
) {
    UNKNOWN(0, ""),
    CV1(1, ""),
    VIVE(2, ""),
    VIVE_PRO(4, ""),
    WMR(8, ""),
    RIFT_S(16, ""),
    QUEST(32, ""),
    INDEX(64, ""),
    COSMOS(128, "");

    companion object {
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        @JvmStatic
        fun fromCode(code: Int): Hmd {
            return Arrays.stream(values()).filter { e: Hmd -> e.code == code }
                .findFirst()
                .orElse(UNKNOWN)
        }
    }
}
