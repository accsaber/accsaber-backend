package de.ixsen.accsaber.api.mapping

import de.ixsen.accsaber.api.dtos.RankedMapDto
import de.ixsen.accsaber.api.mapping.RankedMapMapper
import de.ixsen.accsaber.database.model.maps.BeatMap
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring")
interface RankedMapMapper {
    fun rankedMapsToDtos(BeatMap: List<BeatMap>): List<RankedMapDto>

    @Mapping(source = "song.songHash", target = "songHash")
    @Mapping(source = "song.songName", target = "songName")
    @Mapping(source = "song.songSubName", target = "songSubName")
    @Mapping(source = "song.songAuthorName", target = "songAuthorName")
    @Mapping(source = "song.levelAuthorName", target = "levelAuthorName")
    @Mapping(source = "song.beatSaverKey", target = "beatSaverKey")
    @Mapping(source = "leaderboardId", target = "leaderboardId")
    @Mapping(source = "category.categoryDisplayName", target = "categoryDisplayName")
    fun rankedMapToDto(beatMap: BeatMap): RankedMapDto

    companion object {
        val INSTANCE = Mappers.getMapper(RankedMapMapper::class.java)
    }
}