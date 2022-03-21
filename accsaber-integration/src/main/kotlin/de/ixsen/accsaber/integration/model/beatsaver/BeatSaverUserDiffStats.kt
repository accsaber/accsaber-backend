package de.ixsen.accsaber.integration.model.beatsaver

data class BeatSaverUserDiffStats(
    var easy: Int? = null,
    var normal: Int? = null,
    var hard: Int? = null,
    var expert: Int? = null,
    var expertPlus: Int? = null,
    var total: Int? = null,
)