package de.ixsen.accsaber.api.mapping;

import de.ixsen.accsaber.api.dtos.PlayerScoreDto;
import de.ixsen.accsaber.database.model.players.RankedScore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ScoreMapper {
    ScoreMapper INSTANCE = Mappers.getMapper(ScoreMapper.class);

    @Mapping(target = "techyness", source = "map.techyness")
    @Mapping(target = "songName", source = "map.song.songName")
    @Mapping(target = "songAuthorName", source = "map.song.songAuthorName")
    @Mapping(target = "rank", source = "ranking")
    @Mapping(target = "levelAuthorName", source = "map.song.levelAuthorName")
    @Mapping(target = "difficulty", source = "map.difficulty")
    @Mapping(target = "beatsaverKey", source = "map.song.beatSaverKey")
    PlayerScoreDto rankedScoreToPlayerScore(RankedScore player);

    ArrayList<PlayerScoreDto> rankedScoresToPlayerScores(List<RankedScore> players);
}
