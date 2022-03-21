package de.ixsen.accsaber.api.dtos

class PlayerDto {
    var rank = 0
    var rankLastWeek: Int? = null
    var playerId: String? = null
    var playerName: String? = null
    var avatarUrl: String? = null
    var isAccChamp = false
    var hmd: String? = null
    var averageAcc = 0.0
    var ap = 0.0
    var averageApPerMap = 0.0
    var rankedPlays = 0
}