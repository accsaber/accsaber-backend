package de.ixsen.accsaber.integration.model.beatsaver

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class BeatSaverSongInfo(
    var id: String? = null,
    var metadata: BeatSaverMetadata? = null,
    var versions: List<BeatSaverVersion> = ArrayList(),
)