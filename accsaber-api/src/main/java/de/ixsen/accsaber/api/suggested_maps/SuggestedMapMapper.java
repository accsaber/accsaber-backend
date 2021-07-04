package de.ixsen.accsaber.api.suggested_maps;

import de.ixsen.accsaber.database.model.maps.BeatMap;
import de.ixsen.accsaber.database.model.maps.MapSuggestionVote;
import de.ixsen.accsaber.database.model.staff.StaffUser;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SuggestedMapMapper {

    List<SuggestedMapDto> mapListToDtos(List<BeatMap> beatMaps);

    @Mapping(target = "upVotes", ignore = true)
    @Mapping(target = "staffVote", ignore = true)
    @Mapping(target = "downVotes", ignore = true)
    @Mapping(target = "songSubName", source = "beatMap.song.songSubName")
    @Mapping(target = "songName", source = "beatMap.song.songName")
    @Mapping(target = "songHash", source = "beatMap.song.songHash")
    @Mapping(target = "songAuthorName", source = "beatMap.song.songAuthorName")
    @Mapping(target = "levelAuthorName", source = "beatMap.song.levelAuthorName")
    @Mapping(target = "beatSaverKey", source = "beatMap.song.beatSaverKey")
    SuggestedMapDto mapToDto(BeatMap beatMap, StaffUser staffUser);

    @AfterMapping
    default void mapVotes(BeatMap beatMap, StaffUser staffUser, @MappingTarget SuggestedMapDto suggestedMapDto) {
        List<MapSuggestionVote> mapSuggestionVotes = beatMap.getMapSuggestionVotes()
                .stream().filter(b -> !b.getStaffUser().equals(staffUser))
                .collect(Collectors.toList());

        long upVotes = mapSuggestionVotes.stream().filter(m -> m.getVote() == 1).count();
        long downVotes = mapSuggestionVotes.stream().filter(m -> m.getVote() == -1).count();

        suggestedMapDto.setUpVotes(upVotes);
        suggestedMapDto.setDownVotes(downVotes);

        beatMap.getMapSuggestionVotes()
                .stream()
                .filter(m -> m.getStaffUser().equals(staffUser)).findFirst()
                .ifPresent(mapSuggestionVote -> suggestedMapDto.setStaffVote(mapSuggestionVote.getVote()));
    }
}
