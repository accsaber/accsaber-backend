package de.ixsen.accsaber.api.map_leaderboards;


import de.ixsen.accsaber.database.model.players.RankedScore;
import de.ixsen.accsaber.database.model.players.Score;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapLeaderboardPlayerMapper {

    @Mapping(target = "rank", ignore = true)
    @Mapping(target = "playerName", source = "player.playerName")
    @Mapping(target = "playerId", source = "player.playerId")
    @Mapping(target = "avatarUrl", source = "player.avatarUrl")
    @Mapping(target = "accChamp", source = "player.accChamp")
    MapLeaderboardPlayerDto scoreToMapLeaderboardDto(Score score);

    List<MapLeaderboardPlayerDto> scoresToMapLeaderboardDtos(List<Score> score);

    @Mapping(target = "rank", source = "ranking")
    @Mapping(target = "playerName", source = "player.playerName")
    @Mapping(target = "playerId", source = "player.playerId")
    @Mapping(target = "avatarUrl", source = "player.avatarUrl")
    @Mapping(target = "accChamp", source = "player.accChamp")
    MapLeaderboardPlayerDto rankedScoreToMapLeaderboardDto(RankedScore score);

    List<MapLeaderboardPlayerDto> rankedScoresToMapLeaderboardDtos(List<RankedScore> score);
}
