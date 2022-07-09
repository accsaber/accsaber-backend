package de.ixsen.accsaber.integration.connector

import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverScoreInfo
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import reactor.util.function.Tuple2
import java.util.*

@Service
class BeatSaverConnector : AbstractConnector() {

    private val URL = "https://api.beatsaver.com"

    fun getMapInfoByKey(key: String): BeatSaverSongInfo? {
        val requestUrl = URL + "/maps/id/" + key
        logger.info("RequestUrl {}", requestUrl)
        return getRequest(requestUrl)
            .bodyToMono(BeatSaverSongInfo::class.java)
            .block()
    }

    fun getMapInfoByHash(hash: String): BeatSaverSongInfo? {
        val requestUrl = URL + "/maps/hash/" + hash
        return getRequest(requestUrl)
            .bodyToMono(BeatSaverSongInfo::class.java)
            .block()
    }

    fun getScoreSaberId(hash: String?, diff: Int): Long {
        val requestUrl = String.format("https://beatsaver.com/api/scores/%s/1?difficulty=%d&gameMode=0", hash, diff)
        return getRequest(requestUrl)
            .bodyToMono(BeatSaverScoreInfo::class.java)
            .elapsed() // map the stream's time into our streams data
            .doOnNext { tuple: Tuple2<Long?, BeatSaverScoreInfo?> -> logger.trace("Getting BeatSaver info took {}ms", tuple.t1) }
            .map { obj: Tuple2<Long?, BeatSaverScoreInfo?> -> obj.t2 }
            .map { obj: BeatSaverScoreInfo -> obj?.uid }
            .block()!!
    }

    fun loadCover(mapHash: String): ByteArray? {
        val hash = mapHash.lowercase(Locale.getDefault())
        return getRequest("https://cdn.beatsaver.com/$hash.jpg", MediaType.IMAGE_JPEG)
            .bodyToMono(ByteArray::class.java)
            .block()
    }
}