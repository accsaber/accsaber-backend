package de.ixsen.accsaber.api.mapping;

import de.ixsen.accsaber.api.dtos.PlayerDto;
import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.database.views.AccSaberPlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    @Mapping(target = "rankLastWeek", source = "rankingLastWeek")
    @Mapping(target = "rank", source = "ranking")
    PlayerDto playerToPlayerDto(AccSaberPlayer accSaberPlayer);

    @Mapping(target = "rankedPlays", ignore = true)
    @Mapping(target = "rankLastWeek", ignore = true)
    @Mapping(target = "rank", ignore = true)
    @Mapping(target = "averageApPerMap", ignore = true)
    @Mapping(target = "averageAcc", ignore = true)
    @Mapping(target = "ap", ignore = true)
    PlayerDto rawPlayerToDto(PlayerData accSaberPlayer);

    ArrayList<PlayerDto> playersToPlayerDtos(List<AccSaberPlayer> overallStats);
}
