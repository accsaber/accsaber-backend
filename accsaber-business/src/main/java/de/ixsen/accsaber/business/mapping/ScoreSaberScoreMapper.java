package de.ixsen.accsaber.business.mapping;

import de.ixsen.accsaber.database.model.players.ScoreData;
import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberScoreDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScoreSaberScoreMapper {

    ScoreSaberScoreMapper INSTANCE = Mappers.getMapper(ScoreSaberScoreMapper.class);

    //    @Mapping(target = "timeSet", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    ScoreData scoreSaberScoreDtoToScore(ScoreSaberScoreDto player);


    //    @Mapping(target = "timeSet", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    ScoreData scoreSaberScoreDtoToExistingScore(@MappingTarget ScoreData score, ScoreSaberScoreDto player);
}
