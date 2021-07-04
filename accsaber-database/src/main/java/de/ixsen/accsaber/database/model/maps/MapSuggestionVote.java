package de.ixsen.accsaber.database.model.maps;

import de.ixsen.accsaber.database.model.BaseEntity;
import de.ixsen.accsaber.database.model.staff.StaffUser;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class MapSuggestionVote extends BaseEntity {

    @ManyToOne
    private BeatMap beatMap;

    @ManyToOne
    private StaffUser staffUser;

    /**
     * Can be -1, 0, or 1 for the vote
     */
    private int vote;

    public BeatMap getBeatMap() {
        return this.beatMap;
    }

    public void setBeatMap(BeatMap beatMap) {
        this.beatMap = beatMap;
    }

    public StaffUser getStaffUser() {
        return this.staffUser;
    }

    public void setStaffUser(StaffUser staffUser) {
        this.staffUser = staffUser;
    }

    public int getVote() {
        return this.vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }
}
