package de.ixsen.accsaber.database.model.maps

import javax.persistence.*

@Entity
class Song(
    @Id
    val songHash: String,
    val songName: String,
    val songSubName: String,
    val songAuthorName: String,
    val levelAuthorName: String,
    val beatSaverKey: String,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], mappedBy = "song", orphanRemoval = true)
    val beatMaps: MutableList<BeatMap> = ArrayList(),
)