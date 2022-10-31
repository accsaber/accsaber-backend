package de.ixsen.accsaber.api.mapping

import de.ixsen.accsaber.api.dtos.PlayerScoreDto
import de.ixsen.accsaber.api.mapping.ScoreMapper
import de.ixsen.accsaber.database.views.AccSaberScore
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring")
interface ScoreMapper {
    @Mapping(target = "rank", source = "ranking")
    @Mapping(target = "beatsaverKey", source = "beatSaverKey")
    fun rankedScoreToPlayerScore(accSaberScore: AccSaberScore): PlayerScoreDto
    fun rankedScoresToPlayerScores(accSaberScores: List<AccSaberScore>): ArrayList<PlayerScoreDto>

    companion object {
        val INSTANCE = Mappers.getMapper(ScoreMapper::class.java)
    }
}