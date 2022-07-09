package de.ixsen.accsaber.integration.model.scoresaber.score

class ScoreSaberLeaderboardDto {
    var id: Long = 0
    var songHash: String? = null
    var songName: String? = null
    var songSubName: String? = null
    var songAuthorName: String? = null
    var levelAuthorName: String? = null
    var difficulty: ScoreSaberDifficulty? = null
    var difficultyRaw: String? = null
    var maxScore = 0
    var coverImage: String? = null
}