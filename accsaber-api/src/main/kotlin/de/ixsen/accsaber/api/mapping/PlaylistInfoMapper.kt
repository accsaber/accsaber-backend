package de.ixsen.accsaber.api.mapping

import de.ixsen.accsaber.api.dtos.PlaylistInfoDto
import de.ixsen.accsaber.database.model.Category
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.springframework.beans.factory.annotation.Value

@Mapper(componentModel = "spring")
abstract class PlaylistInfoMapper {
    @Value("\${accsaber.image-serving-url}")
    private var imageServingUrl: String? = null

    @Value("\${accsaber.playlist-url}")
    private var playlistUrl: String? = null

    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "downloadLink", ignore = true)
    @Mapping(source = "categoryName", target = "name")
    @Mapping(source = "categoryDisplayName", target = "displayName")
    @Mapping(source = "description", target = "description")
    abstract fun map(category: Category): PlaylistInfoDto
    abstract fun map(category: List<Category>): ArrayList<PlaylistInfoDto>

    @AfterMapping
    protected fun addUrls(category: Category, @MappingTarget playlistInfoDto: PlaylistInfoDto) {
        playlistInfoDto.downloadLink = String.format("%s%s", playlistUrl, category.categoryName)
        playlistInfoDto.imageUrl = String.format("%splaylists/%s.png", imageServingUrl, category.categoryName)
    }
}