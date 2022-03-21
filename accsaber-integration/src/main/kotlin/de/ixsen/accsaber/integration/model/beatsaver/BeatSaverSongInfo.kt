package de.ixsen.accsaber.integration.model.beatsaver

data class BeatSaverSongInfo(
    var isAutomapper: Boolean? = null,
    var curator: String? = null,
    var description: String? = null,
    var id: String? = null,
    var metadata: BeatSaverMetadata? = null,
    var name: String? = null,
    var isQualified: Boolean? = null,
    var isRanked: Boolean? = null,
    var stats: BeatSaverStats? = null,
    var uploaded: String? = null,
    var uploader: BeatSaverUploader? = null,
    var versions: List<BeatSaverVersion> = ArrayList(),
)