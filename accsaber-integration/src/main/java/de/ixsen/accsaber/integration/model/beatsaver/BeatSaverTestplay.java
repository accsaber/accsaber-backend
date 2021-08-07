package de.ixsen.accsaber.integration.model.beatsaver;

public class BeatSaverTestplay {
    private String createdAt;

    private String feedback;

    private String feedbackAt;

    private BeatSaverUploader user;

    private String video;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedbackAt() {
        return feedbackAt;
    }

    public void setFeedbackAt(String feedbackAt) {
        this.feedbackAt = feedbackAt;
    }

    public BeatSaverUploader getUser() {
        return user;
    }

    public void setUser(BeatSaverUploader user) {
        this.user = user;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
