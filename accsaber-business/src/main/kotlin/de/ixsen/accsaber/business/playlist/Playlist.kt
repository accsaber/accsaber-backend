package de.ixsen.accsaber.business.playlist

class Playlist
    (
    val playlistTitle: String?,
    val playlistAuthor: String?,
    val image: String?,
    val syncURL: String,
    val songs: MutableList<PlaylistSong> = ArrayList()
)