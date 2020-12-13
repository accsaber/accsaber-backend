package de.ixsen.accsaber.business.mapping;

import de.ixsen.accsaber.database.model.players.Player;
import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberPlayerInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScoreSaberPlayerMapper {

    ScoreSaberPlayerMapper INSTANCE = Mappers.getMapper(ScoreSaberPlayerMapper.class);

    @Mapping(target = "playerId", ignore = true)
    @Mapping(target = "avatarUrl", source = "avatar")
    void scoreSaberPlayerToPlayer(@MappingTarget Player player, ScoreSaberPlayerInfoDto playerDto);
}
