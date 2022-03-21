package de.ixsen.accsaber.api.dtos

class PlayerScoreDto {
    var rank: Long = 0
    var ap = 0.0
    var weightedAp = 0.0
    var score: Long = 0
    var accuracy = 0.0
    var songName: String? = null
    var songAuthorName: String? = null
    var levelAuthorName: String? = null
    var complexity = 0.0
    var songHash: String? = null
    var difficulty: String? = null
    var leaderboardId: String? = null
    var beatsaverKey: String? = null
    var timeSet: String? = null
    var categoryDisplayName: String? = null
}