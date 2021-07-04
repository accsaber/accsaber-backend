package de.ixsen.accsaber.api.players;

import de.ixsen.accsaber.database.model.players.RankedPlayer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    @Mapping(target = "rank", source = "ranking")
    PlayerDto playerToPlayerDto(RankedPlayer player);

    ArrayList<PlayerDto> playersToPlayerDtos(List<RankedPlayer> players);
}
