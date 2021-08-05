package de.ixsen.accsaber.integration.model.beatsaver;

import java.util.List;

public class BeatSaverSongInfo {
    private boolean automapper;

    private String curator;

    private String description;

    private String id;

    private BeatSaverMetadata metadata;

    private String name;

    private boolean qualified;

    private boolean ranked;

    private BeatSaverStats stats;

    private String uploaded;

    private BeatSaverUploader uploader;

    private List<BeatSaverVersion> versions;

    public boolean isAutomapper() {
        return automapper;
    }

    public void setAutomapper(boolean automapper) {
        this.automapper = automapper;
    }

    public String getCurator() {
        return curator;
    }

    public void setCurator(String curator) {
        this.curator = curator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BeatSaverMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(BeatSaverMetadata metadata) {
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isQualified() {
        return qualified;
    }

    public void setQualified(boolean qualified) {
        this.qualified = qualified;
    }

    public boolean isRanked() {
        return ranked;
    }

    public void setRanked(boolean ranked) {
        this.ranked = ranked;
    }

    public BeatSaverStats getStats() {
        return stats;
    }

    public void setStats(BeatSaverStats stats) {
        this.stats = stats;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public BeatSaverUploader getUploader() {
        return uploader;
    }

    public void setUploader(BeatSaverUploader uploader) {
        this.uploader = uploader;
    }

    public List<BeatSaverVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<BeatSaverVersion> versions) {
        this.versions = versions;
    }
}
