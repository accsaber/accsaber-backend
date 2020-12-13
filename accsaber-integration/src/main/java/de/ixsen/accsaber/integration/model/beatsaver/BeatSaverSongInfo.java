package de.ixsen.accsaber.integration.model.beatsaver;

public class BeatSaverSongInfo {
    private BeatSaverMetadata metadata;

    private BeatSaverStats stats;

    private String description;

    private String deletedAt;

    private String _id;

    private String key;

    private String name;

    private BeatSaverUploader uploader;

    private String hash;

    private String uploaded;

    private String directDownload;

    private String downloadURL;

    private String coverURL;

    public void setMetadata(BeatSaverMetadata metadata) {
        this.metadata = metadata;
    }

    public BeatSaverMetadata getMetadata() {
        return this.metadata;
    }

    public void setStats(BeatSaverStats stats) {
        this.stats = stats;
    }

    public BeatSaverStats getStats() {
        return this.stats;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedAt() {
        return this.deletedAt;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return this._id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setUploader(BeatSaverUploader uploader) {
        this.uploader = uploader;
    }

    public BeatSaverUploader getUploader() {
        return this.uploader;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return this.hash;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }

    public String getUploaded() {
        return this.uploaded;
    }

    public void setDirectDownload(String directDownload) {
        this.directDownload = directDownload;
    }

    public String getDirectDownload() {
        return this.directDownload;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getDownloadURL() {
        return this.downloadURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getCoverURL() {
        return this.coverURL;
    }
}
