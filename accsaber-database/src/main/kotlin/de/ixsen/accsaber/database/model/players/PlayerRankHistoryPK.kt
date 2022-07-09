package de.ixsen.accsaber.database.model.players

import java.io.Serializable
import java.time.LocalDate
import java.util.*

class PlayerRankHistoryPK : Serializable {
    protected var player: Long? = null
    protected var date: LocalDate? = null
    protected var category: Long? = null

    constructor() {}
    constructor(player: Long?, date: LocalDate?) {
        this.player = player
        this.date = date
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || this.javaClass != o.javaClass) return false
        val that = o as PlayerRankHistoryPK
        return player == that.player && date == that.date && this.category == that.category
    }

    override fun hashCode(): Int {
        return Objects.hash(player, date, this.category)
    }
}