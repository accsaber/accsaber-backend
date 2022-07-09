package de.ixsen.accsaber.integration.connector

import de.ixsen.accsaber.integration.model.scoresaber.player.ScoreSaberPlayerDto
import de.ixsen.accsaber.integration.model.scoresaber.score.ScoreSaberScoreListDto
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.UnsupportedMediaTypeException
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.stream.Collectors

@Service
class ScoreSaberConnector : AbstractConnector() {

    private val recentRequests: MutableSet<Instant> = ConcurrentHashMap.newKeySet()
    private var pauseInstant = Instant.MIN

    // TODO make configurable
    private val url = "https://scoresaber.com/api"


    @Throws(InterruptedException::class)
    fun getPlayerData(playerId: Long?): ScoreSaberPlayerDto? {
        handleConnectionLimit()
        val requestUrl = String.format("%s/player/%s/full", url, playerId)
        return getRequest(requestUrl)
            .bodyToMono(ScoreSaberPlayerDto::class.java)
            .onErrorResume(WebClientResponseException.NotFound::class.java) { notFound: WebClientResponseException.NotFound? -> Mono.empty() }
            .doOnError(WebClientResponseException.TooManyRequests::class.java) { tooManyRequests: WebClientResponseException.TooManyRequests? ->
                logger.warn("Too many requests reached, pausing...")
                pauseInstant = Instant.now()
                throw RuntimeException()
            }
            .doOnError(UnsupportedMediaTypeException::class.java) { unsupportedMediaType: UnsupportedMediaTypeException? ->
                logger.warn("Too many requests reached, pausing...")
                pauseInstant = Instant.now()
                throw RuntimeException()
            }
            .block()
    }

    // TODO make page size configurable, remember in player service during calc
    @Throws(InterruptedException::class)
    fun getPlayerScores(playerId: Long?, page: Int): ScoreSaberScoreListDto? {
        handleConnectionLimit()
        val requestUrl = String.format("%s/player/%s/scores?page=%d&sort=recent&limit=100", url, playerId, page)
        return getRequest(requestUrl)
            .bodyToMono(ScoreSaberScoreListDto::class.java)
            .onErrorResume(WebClientResponseException.NotFound::class.java) { notFound: WebClientResponseException.NotFound? -> Mono.empty() }
            .doOnError(WebClientResponseException.TooManyRequests::class.java) { tooManyRequests: WebClientResponseException.TooManyRequests? ->
                logger.warn("Too many requests reached, pausing...")
                pauseInstant = Instant.now()
                throw RuntimeException()
            }
            .doOnError(UnsupportedMediaTypeException::class.java) { unsupportedMediaType: UnsupportedMediaTypeException? ->
                logger.warn("Too many requests reached, pausing...")
                pauseInstant = Instant.now()
                throw RuntimeException()
            }
            .block()
    }

    fun loadAvatar(avatarUrl: String): ByteArray? {
        return getRequest(avatarUrl, MediaType.IMAGE_JPEG)
            .bodyToMono(ByteArray::class.java)
            .block()
    }

    @Throws(InterruptedException::class)
    private fun handleConnectionLimit() {
        recentRequests.stream()
            .filter { instant: Instant -> instant.isBefore(Instant.now().minusSeconds(60)) }.collect(Collectors.toList())
            .forEach(Consumer { o: Instant -> recentRequests.remove(o) })
        val currentMaxSize = recentRequests.size
        if (maxRequestsThisSession < currentMaxSize) {
            maxRequestsThisSession = currentMaxSize
        }
        if (currentMaxSize > 390 || pauseInstant.isAfter(Instant.now().minusSeconds(10))) {
            TimeUnit.SECONDS.sleep(1)
            handleConnectionLimit()
            return
        }
        recentRequests.add(Instant.now())
    }

    companion object {
        var maxRequestsThisSession = 0
    }
}

