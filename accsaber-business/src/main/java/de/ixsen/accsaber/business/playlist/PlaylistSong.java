package de.ixsen.accsaber.business.playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSong {
    private String hash;
    private String songName;
    private final List<PlaylistSongDifficulty> difficulties;

    public PlaylistSong() {
        this.difficulties = new ArrayList<>();
    }

    public String getHash() {
        return this.hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSongName() {
        return this.songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public List<PlaylistSongDifficulty> getDifficulties() {
        return this.difficulties;
    }
}
