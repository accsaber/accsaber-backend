package de.ixsen.accsaber.integration.model.beatsaver

data class BeatSaverUserStats(
    var avgBpm: Double? = null,
    var avgDuration: Double? = null,
    var avgScore: Double? = null,
    var diffStats: BeatSaverUserDiffStats? = null,
    var firstUpload: String? = null,
    var lastUpload: String? = null,
    var rankedMaps: Int? = null,
    var totalDownvotes: Int? = null,
    var totalMaps: Int? = null,
    var totalUpvotes: Int? = null,
)