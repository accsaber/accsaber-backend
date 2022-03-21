package de.ixsen.accsaber.database.model

import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OrderBy

@Entity
class Category(
    /**
     * Potential for the future:
     * Different calcs for different leaderboards, steeper curve for true acc, less steep for tech etc
     */
    @Column(unique = true, nullable = false)
    val categoryName: String,
    val description: String,
    val categoryDisplayName: String,
    val countsTowardsOverall: Boolean,

    @OrderBy
    @CreatedDate
    @Column(nullable = false, updatable = false)
    val createdDate: Instant = Instant.now(),

    @Column(name = "player_curve_y1")
    @ColumnDefault("0.1")
    var playerCurveY1: Double = 0.1,

    @Column(name = "player_curve_x1")
    @ColumnDefault("15")
    var playerCurveX1: Double = 15.0,

    @Column(name = "player_curve_k")
    @ColumnDefault("0.4")
    var playerCurveK: Double = 0.4,

    @Column(name = "ap_curve_a")
    @ColumnDefault("1.036")
    var apCurveA: Double = 1.036,

    @Column(name = "ap_curve_b")
    @ColumnDefault("62")
    var apCurveB: Double = 62.0,

    @Column(name = "ap_curve_c")
    @ColumnDefault("10")
    var apCurveC: Double = 10.0,

    @Column(name = "ap_curve_d")
    @ColumnDefault("15.5")
    var apCurveD: Double = 15.5,

    @Column(name = "ap_curve_e")
    @ColumnDefault("10")
    var apCurveE: Double = 10.0
) : BaseEntity()