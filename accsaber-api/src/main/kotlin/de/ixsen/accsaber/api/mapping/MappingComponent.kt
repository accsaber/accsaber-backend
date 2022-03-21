package de.ixsen.accsaber.api.mapping

import org.springframework.stereotype.Component

@Component
class MappingComponent {
    val playerMapper: PlayerMapper
        get() = PlayerMapper.Companion.INSTANCE
    val rankedMapMapper: RankedMapMapper
        get() = RankedMapMapper.Companion.INSTANCE
    val mapLeaderboardPlayerMapper: MapLeaderboardPlayerMapper
        get() = MapLeaderboardPlayerMapper.Companion.INSTANCE
    val scoreMapper: ScoreMapper
        get() = ScoreMapper.Companion.INSTANCE
}