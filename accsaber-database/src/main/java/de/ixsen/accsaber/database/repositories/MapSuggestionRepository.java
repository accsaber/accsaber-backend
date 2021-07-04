package de.ixsen.accsaber.database.repositories;

import de.ixsen.accsaber.database.model.maps.MapSuggestionVote;
import de.ixsen.accsaber.database.model.staff.StaffUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MapSuggestionRepository extends JpaRepository<MapSuggestionVote, Long> {

    //    @Query("from MapSuggestion suggestion " +
//            "join fetch BeatMap beatmap " +
//            "on suggestion.beatMap = beatmap " +
//            "where beatMap.leaderboardId = ?1")
    @Query("from MapSuggestionVote suggestion where suggestion.beatMap.leaderboardId = ?1 and suggestion.staffUser = ?2")
    MapSuggestionVote findSuggestionByLeaderboardIdAndStaffUser(Long id, StaffUser staffUser);
}
