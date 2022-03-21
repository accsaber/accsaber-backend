package de.ixsen.accsaber.integration.model.beatsaver

data class BeatSaverUploader(
    var avatar: String? = null,
    var hash: String? = null,
    var id: Int? = null,
    var name: String? = null,
    var stats: BeatSaverUserStats? = null,
    var isTestplay: Boolean? = null
)