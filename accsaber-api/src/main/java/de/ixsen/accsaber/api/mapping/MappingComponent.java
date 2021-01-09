package de.ixsen.accsaber.api.mapping;

import org.springframework.stereotype.Component;

@Component
public class MappingComponent {

    public PlayerMapper getPlayerMapper() {
        return PlayerMapper.INSTANCE;
    }

    public RankedMapMapper getRankedMapMapper() {
        return RankedMapMapper.INSTANCE;
    }

    public MapLeaderboardPlayerMapper getMapLeaderboardPlayerMapper() {
        return MapLeaderboardPlayerMapper.INSTANCE;
    }

    public ScoreMapper getScoreMapper() {
        return ScoreMapper.INSTANCE;
    }
}
