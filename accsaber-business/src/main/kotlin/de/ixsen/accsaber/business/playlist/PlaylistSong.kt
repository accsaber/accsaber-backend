package de.ixsen.accsaber.business.playlist

data class PlaylistSong (
    val hash: String,
    val songName: String,
    val difficulties: MutableList<PlaylistSongDifficulty> = ArrayList()
)