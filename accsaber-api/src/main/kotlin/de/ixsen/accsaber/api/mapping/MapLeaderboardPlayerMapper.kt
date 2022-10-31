package de.ixsen.accsaber.api.mapping

import de.ixsen.accsaber.api.dtos.MapLeaderboardPlayerDto
import de.ixsen.accsaber.api.mapping.MapLeaderboardPlayerMapper
import de.ixsen.accsaber.database.views.AccSaberScore
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring")
interface MapLeaderboardPlayerMapper {
    @Mapping(target = "categoryName", source = "categoryDisplayName")
    @Mapping(target = "rank", source = "ranking")
    @Mapping(target = "playerName", source = "player.playerName")
    @Mapping(target = "playerId", source = "player.playerId")
    @Mapping(target = "avatarUrl", source = "player.avatarUrl")
    @Mapping(target = "accChamp", source = "player.accChamp")
    fun rankedScoreToMapLeaderboardDto(score: AccSaberScore): MapLeaderboardPlayerDto
    fun rankedScoresToMapLeaderboardDtos(score: List<AccSaberScore>): List<MapLeaderboardPlayerDto>

    companion object {
        val INSTANCE = Mappers.getMapper(MapLeaderboardPlayerMapper::class.java)
    }
}