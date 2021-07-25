package de.ixsen.accsaber.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Category extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String categoryName;

    private String description;

    /**
     * Potential for the future:
     * Different calcs for different leaderboards, steeper curve for true acc, less steep for tech etc
     */

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
