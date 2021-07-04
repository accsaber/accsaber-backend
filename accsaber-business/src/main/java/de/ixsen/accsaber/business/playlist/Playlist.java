package de.ixsen.accsaber.business.playlist;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private String playlistTitle;
    private String playlistAuthor;
    private String image;
    private List<PlaylistSong> songs;
    // TODO Make configurable
    private final String syncURL = "https://www.accsaber.ixsen.de/api/ranked-maps/playlist";

    public Playlist() {
        this.songs = new ArrayList<>();
    }

    public String getPlaylistTitle() {
        return this.playlistTitle;
    }

    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }

    public String getPlaylistAuthor() {
        return this.playlistAuthor;
    }

    public void setPlaylistAuthor(String playlistAuthor) {
        this.playlistAuthor = playlistAuthor;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PlaylistSong> getSongs() {
        return this.songs;
    }

    public void setSongs(List<PlaylistSong> songs) {
        this.songs = songs;
    }

    public String getSyncURL() {
        return this.syncURL;
    }
}
