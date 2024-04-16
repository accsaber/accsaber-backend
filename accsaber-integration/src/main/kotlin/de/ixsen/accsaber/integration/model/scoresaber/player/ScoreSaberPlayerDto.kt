package de.ixsen.accsaber.integration.model.scoresaber.player

data class ScoreSaberPlayerDto(
    var id: String? = null,
    var name: String? = null,
    var profilePicture: String? = null,
    var country: String? = null,
    var scoreStats: ScoreSaberScoreStatsDto? = null,
    var banned: Boolean? = false,
    var inactive: Boolean? = false,
)
