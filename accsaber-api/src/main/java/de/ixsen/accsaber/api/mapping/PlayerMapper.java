package de.ixsen.accsaber.api.mapping;

import de.ixsen.accsaber.api.dtos.PlayerDto;
import de.ixsen.accsaber.database.model.players.RankedPlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    @Mapping(target = "rank", source = "ranking")
    PlayerDto playerToPlayerDto(RankedPlayer player);

    ArrayList<PlayerDto> playersToPlayerDtos(List<RankedPlayer> players);
}
