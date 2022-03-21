package de.ixsen.accsaber.business.mapping

import org.springframework.stereotype.Component

@Component
class BusinessMappingComponent {
    val scoreMapper: ScoreSaberScoreMapper
        get() = ScoreSaberScoreMapper.Companion.INSTANCE
    val playerMapper: ScoreSaberPlayerMapper
        get() = ScoreSaberPlayerMapper.Companion.INSTANCE
}