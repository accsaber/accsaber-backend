package de.ixsen.accsaber.integration.model.beatsaver

data class BeatSaverTestplay(
    var createdAt: String? = null,
    var feedback: String? = null,
    var feedbackAt: String? = null,
    var user: BeatSaverUploader? = null,
    var video: String? = null,
)