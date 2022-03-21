package de.ixsen.accsaber.business.mapping;

import de.ixsen.accsaber.database.model.players.PlayerData;
import de.ixsen.accsaber.integration.model.scoresaber.player.ScoreSaberPlayerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScoreSaberPlayerMapper {

    ScoreSaberPlayerMapper INSTANCE = Mappers.getMapper(ScoreSaberPlayerMapper.class);

    @Mapping(target = "scores", ignore = true)
    @Mapping(target = "playerCategoryStats", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "hmd", ignore = true)
    @Mapping(target = "accChamp", ignore = true)
    @Mapping(target = "playerName", source = "name")
    @Mapping(target = "playerId", source = "id")
    @Mapping(target = "avatarUrl", source = "profilePicture")
    void scoreSaberPlayerToPlayer(@MappingTarget PlayerData player, ScoreSaberPlayerDto playerDto);
}
