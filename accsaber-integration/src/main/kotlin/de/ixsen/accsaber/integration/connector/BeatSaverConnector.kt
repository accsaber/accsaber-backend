package de.ixsen.accsaber.integration.connector

import de.ixsen.accsaber.integration.connector.BeatSaverConnector
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverScoreInfo
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.function.Tuple2
import java.util.*

@Service
class BeatSaverConnector @Autowired constructor() {
    private val webClient: WebClient

    init {
        webClient = WebClient.create()
    }

    fun getMapInfoByKey(key: String): BeatSaverSongInfo? {
        val requestUrl = URL + "/maps/id/" + key
        logger.info("RequestUrl {}", requestUrl)
        return webClient.get().uri(requestUrl)
            .exchangeToMono { clientResponse: ClientResponse ->
                clientResponse.bodyToMono(
                    BeatSaverSongInfo::class.java
                )
            }
            .block()
    }

    fun getMapInfoByHash(hash: String): BeatSaverSongInfo? {
        val requestUrl = URL + "/maps/hash/" + hash
        return webClient.get().uri(requestUrl)
            .exchangeToMono { clientResponse: ClientResponse ->
                clientResponse.bodyToMono(
                    BeatSaverSongInfo::class.java
                )
            }
            .elapsed() // map the stream's time into our streams data
            .doOnNext { tuple: Tuple2<Long?, BeatSaverSongInfo?> -> logger.trace("Getting BeatSaver info took {}ms", tuple.t1) }
            .map { obj: Tuple2<Long?, BeatSaverSongInfo?> -> obj.t2 }
            .block()
    }

    fun getScoreSaberId(hash: String?, diff: Int): Long {
        val requestUrl = String.format("https://beatsaver.com/api/scores/%s/1?difficulty=%d&gameMode=0", hash, diff)
        return webClient.get().uri(requestUrl)
            .exchangeToMono { clientResponse: ClientResponse ->
                clientResponse.bodyToMono(
                    BeatSaverScoreInfo::class.java
                )
            }
            .elapsed() // map the stream's time into our streams data
            .doOnNext { tuple: Tuple2<Long?, BeatSaverScoreInfo?> -> logger.trace("Getting BeatSaver info took {}ms", tuple.t1) }
            .map { obj: Tuple2<Long?, BeatSaverScoreInfo?> -> obj.t2 }
            .map { obj: BeatSaverScoreInfo -> obj.uid }
            .block()!!
    }

    fun loadCover(hash: String): ByteArray? {
        return webClient.get().uri("https://cdn.beatsaver.com/" + hash.lowercase(Locale.getDefault()) + ".jpg")
            .accept(MediaType.IMAGE_JPEG)
            .retrieve()
            .bodyToMono(ByteArray::class.java)
            .block()
    }

    companion object {
        const val URL = "https://api.beatsaver.com"
        val logger = LoggerFactory.getLogger(BeatSaverConnector::class.java)
    }
}