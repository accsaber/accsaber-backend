package de.ixsen.accsaber.integration.model.beatsaver

data class BeatSaverStats(
    var downloads: Int? = null,
    var downvotes: Int? = null,
    var plays: Int? = null,
    var score: Double? = null,
    var upvotes: Int? = null
)