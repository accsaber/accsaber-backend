package de.ixsen.accsaber.business.mapping

import de.ixsen.accsaber.database.model.players.ScoreData
import de.ixsen.accsaber.integration.model.scoresaber.score.ScoreSaberScoreBundleDto
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers

@Mapper
interface ScoreSaberScoreMapper {
    @Mapping(target = "weightedAp", ignore = true)
    @Mapping(target = "rankedScore", ignore = true)
    @Mapping(target = "rankWhenScoresSet", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "beatMap", ignore = true)
    @Mapping(target = "ap", ignore = true)
    @Mapping(target = "accuracy", ignore = true)
    @Mapping(target = "mods", source = "score.modifiers")
    @Mapping(target = "leaderboardId", source = "leaderboard.id")
    @Mapping(target = "unmodififiedScore", source = "score.baseScore")
    @Mapping(target = "score", source = "score.modifiedScore")
    @Mapping(target = "timeSet", source = "score.timeSet")
    @Mapping(target = "scoreId", source = "score.id")
    fun scoreSaberScoreDtoToScore(player: ScoreSaberScoreBundleDto): ScoreData

    @Mapping(target = "weightedAp", ignore = true)
    @Mapping(target = "rankedScore", ignore = true)
    @Mapping(target = "rankWhenScoresSet", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "beatMap", ignore = true)
    @Mapping(target = "ap", ignore = true)
    @Mapping(target = "accuracy", ignore = true)
    @Mapping(target = "scoreId", ignore = true)
    @Mapping(target = "mods", source = "score.modifiers")
    @Mapping(target = "leaderboardId", source = "leaderboard.id")
    @Mapping(target = "unmodififiedScore", source = "score.baseScore")
    @Mapping(target = "score", source = "score.modifiedScore")
    @Mapping(target = "timeSet", source = "score.timeSet")
    fun scoreSaberScoreDtoToExistingScore(@MappingTarget score: ScoreData, player: ScoreSaberScoreBundleDto): ScoreData

    companion object {
        val INSTANCE = Mappers.getMapper(ScoreSaberScoreMapper::class.java)
    }
}