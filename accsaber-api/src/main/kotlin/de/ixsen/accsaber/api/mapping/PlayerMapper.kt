package de.ixsen.accsaber.api.mapping

import de.ixsen.accsaber.api.dtos.PlayerDto
import de.ixsen.accsaber.api.mapping.PlayerMapper
import de.ixsen.accsaber.database.model.players.PlayerData
import de.ixsen.accsaber.database.views.AccSaberPlayer
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper
interface PlayerMapper {
    @Mapping(target = "rankLastWeek", source = "rankingLastWeek")
    @Mapping(target = "rank", source = "ranking")
    fun playerToPlayerDto(accSaberPlayer: AccSaberPlayer): PlayerDto

    @Mapping(target = "rankedPlays", ignore = true)
    @Mapping(target = "rankLastWeek", ignore = true)
    @Mapping(target = "rank", ignore = true)
    @Mapping(target = "averageApPerMap", ignore = true)
    @Mapping(target = "averageAcc", ignore = true)
    @Mapping(target = "ap", ignore = true)
    fun rawPlayerToDto(accSaberPlayer: PlayerData): PlayerDto
    fun playersToPlayerDtos(overallStats: List<AccSaberPlayer>): ArrayList<PlayerDto>

    companion object {
        val INSTANCE: PlayerMapper = Mappers.getMapper(PlayerMapper::class.java)
    }
}