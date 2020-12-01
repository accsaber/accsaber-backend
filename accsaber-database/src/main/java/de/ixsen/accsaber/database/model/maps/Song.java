package de.ixsen.accsaber.database.model.maps;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Song {

    @Id
    private String songHash;

    private String songName;
    private String songSubName;
    private String songAuthorName;
    private String levelAuthorName;

    @OneToMany(fetch = FetchType.EAGER)
    private List<RankedMap> rankedMaps;

    private String beatSaverKey;

    public String getSongHash() {
        return this.songHash;
    }

    public void setSongHash(String songHash) {
        this.songHash = songHash;
    }

    public String getSongName() {
        return this.songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongSubName() {
        return this.songSubName;
    }

    public void setSongSubName(String songSubName) {
        this.songSubName = songSubName;
    }

    public String getSongAuthorName() {
        return this.songAuthorName;
    }

    public void setSongAuthorName(String songAuthorName) {
        this.songAuthorName = songAuthorName;
    }

    public String getLevelAuthorName() {
        return this.levelAuthorName;
    }

    public void setLevelAuthorName(String levelAuthorName) {
        this.levelAuthorName = levelAuthorName;
    }

    public List<RankedMap> getRankedMaps() {
        return this.rankedMaps;
    }

    public void setRankedMaps(List<RankedMap> rankedMaps) {
        this.rankedMaps = rankedMaps;
    }

    public String getBeatSaverKey() {
        return this.beatSaverKey;
    }

    public void setBeatSaverKey(String beatSaverKey) {
        this.beatSaverKey = beatSaverKey;
    }
}
