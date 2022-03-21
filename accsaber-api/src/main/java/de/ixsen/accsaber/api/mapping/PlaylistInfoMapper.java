package de.ixsen.accsaber.api.mapping;

import de.ixsen.accsaber.api.dtos.PlaylistInfoDto;
import de.ixsen.accsaber.database.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PlaylistInfoMapper {
    @Value("${accsaber.image-serving-url}")
    private String imageServingUrl;
    @Value("${accsaber.playlist-url}")
    private String playlistUrl;

    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "downloadLink", ignore = true)
    @Mapping(source = "categoryName", target = "name")
    @Mapping(source = "categoryDisplayName", target = "displayName")
    @Mapping(source = "description", target = "description")
    public abstract PlaylistInfoDto map(Category category);

    public abstract ArrayList<PlaylistInfoDto> map(List<Category> category);

    @AfterMapping
    protected void addUrls(Category category, @MappingTarget PlaylistInfoDto playlistInfoDto) {
        playlistInfoDto.setDownloadLink(String.format("%s%s", this.playlistUrl, category.getCategoryName()));
        playlistInfoDto.setImageUrl(String.format("%splaylists/%s.png", this.imageServingUrl, category.getCategoryName()));
    }
}
