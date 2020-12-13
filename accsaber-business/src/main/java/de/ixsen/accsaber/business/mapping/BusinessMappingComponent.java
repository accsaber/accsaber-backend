package de.ixsen.accsaber.business.mapping;

import org.springframework.stereotype.Component;

@Component
public class BusinessMappingComponent {

    public ScoreSaberScoreMapper getScoreMapper() {
        return ScoreSaberScoreMapper.INSTANCE;
    }

    public ScoreSaberPlayerMapper getPlayerMapper() {
        return ScoreSaberPlayerMapper.INSTANCE;
    }
}
