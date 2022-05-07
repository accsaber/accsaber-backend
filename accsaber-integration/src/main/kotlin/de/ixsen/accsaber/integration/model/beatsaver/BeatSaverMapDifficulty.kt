package de.ixsen.accsaber.integration.model.beatsaver

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BeatSaverMapDifficulty(
    var bombs: Int? = null,
    var characteristic: String? = null,
    var isChroma: Boolean? = null,
    var isCinema: Boolean? = null,
    var difficulty: String? = null,
    var events: Int? = null,
    var length: Double? = null,
    var isMe: Boolean? = null,
    var isNe: Boolean? = null,
    var njs: Double? = null,
    var notes: Int? = null,
    var nps: Double? = null,
    var obstacles: Int? = null,
    var offset: Double? = null,
    var paritySummary: BeatSaverMapParitySummary? = null,
    var seconds: Double? = null,
    var stars: Double? = null,
)