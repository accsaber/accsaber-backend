package de.ixsen.accsaber.database.model

import java.io.Serializable
import java.util.*

class PlayerCategoryStatsPK : Serializable {
    protected var player: Long? = null
    protected var category: Long? = null

    constructor() {}
    constructor(player: Long?, category: Long?) {
        this.player = player
        this.category = category
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || this.javaClass != o.javaClass) return false
        val that = o as PlayerCategoryStatsPK
        return player == that.player && this.category == that.category
    }

    override fun hashCode(): Int {
        return Objects.hash(player, this.category)
    }
}