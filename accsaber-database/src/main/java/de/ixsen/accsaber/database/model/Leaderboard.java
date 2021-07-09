package de.ixsen.accsaber.database.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Leaderboard  {
    @Id
    private String name;

    private String description;

    /**
     *     Potential for the future:
     *     Different calcs for different leaderboards, steeper curve for true acc, less steep for tech etc
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
