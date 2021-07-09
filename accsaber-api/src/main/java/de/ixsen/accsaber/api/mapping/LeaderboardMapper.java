package de.ixsen.accsaber.api.mapping;

import de.ixsen.accsaber.api.dtos.LeaderboardDto;
import de.ixsen.accsaber.database.model.Leaderboard;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeaderboardMapper {
    LeaderboardDto map(Leaderboard leaderboard);

    Leaderboard map(LeaderboardDto leaderboardDto);

    List<LeaderboardDto> map(List<Leaderboard> leaderboards);
}
