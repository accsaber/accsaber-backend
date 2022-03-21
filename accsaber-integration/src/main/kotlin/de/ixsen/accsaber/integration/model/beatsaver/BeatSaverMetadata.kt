package de.ixsen.accsaber.integration.model.beatsaver

data class BeatSaverMetadata(
    var bpm: Double? = null,
    var duration: Int? = null,
    var levelAuthorName: String? = null,
    var songAuthorName: String? = null,
    var songName: String? = null,
    var songSubName: String? = null,
)