package de.ixsen.accsaber.integration.model.beatsaver

data class BeatSaverVersion(
    var coverURL: String? = null,
    var createdAt: String? = null,
    var downloadURL: String? = null,
    var feedback: String? = null,
    var hash: String? = null,
    var key: String? = null,
    var previewURL: String? = null,
    var sageScore: Int? = null,
    var state: String? = null,
    var testplayAt: String? = null,
    var diffs: List<BeatSaverMapDifficulty> = ArrayList(),
    var testplays: List<BeatSaverTestplay> = ArrayList(),
)