package de.ixsen.accsaber.api.mapping;

import java.util.List;

import de.ixsen.accsaber.api.dtos.RankedMapDto;
import de.ixsen.accsaber.database.model.maps.BeatMap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RankedMapMapper {

    RankedMapMapper INSTANCE = Mappers.getMapper(RankedMapMapper.class);

    List<RankedMapDto> rankedMapsToDtos(List<BeatMap> BeatMap);

    @Mapping(source = "song.songHash", target = "songHash")
    @Mapping(source = "song.songName", target = "songName")
    @Mapping(source = "song.songSubName", target = "songSubName")
    @Mapping(source = "song.songAuthorName", target = "songAuthorName")
    @Mapping(source = "song.levelAuthorName", target = "levelAuthorName")
    @Mapping(source = "song.beatSaverKey", target = "beatsaverKey")
    @Mapping(source = "leaderboardId", target = "leaderboardId")
    RankedMapDto rankedMapToDto(BeatMap beatMap);
}
