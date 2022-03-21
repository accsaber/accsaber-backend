package de.ixsen.accsaber.integration.connector;

import de.ixsen.accsaber.integration.model.scoresaber.player.ScoreSaberPlayerDto;
import de.ixsen.accsaber.integration.model.scoresaber.score.ScoreSaberScoreListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.UnsupportedMediaTypeException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ScoreSaberConnector {

    // TODO make configurable
    public static final String URL = "https://scoresaber.com/api";
    private final WebClient webClient;

    private final Set<Instant> recentRequests;
    private Instant pauseInstant = Instant.MIN;
    private static int max;

    @Autowired
    public ScoreSaberConnector(RestTemplateBuilder restTemplateBuilder) {
        this.webClient = WebClient.create();
        this.recentRequests = ConcurrentHashMap.newKeySet();
    }

    public Optional<ScoreSaberPlayerDto> getPlayerData(Long playerId) throws InterruptedException {
        this.handleConnectionLimit();

        String requestUrl = String.format("%s/player/%s/full", URL, playerId);

        return this.webClient.get().uri(requestUrl)
                .retrieve()
                .bodyToMono(ScoreSaberPlayerDto.class)
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                .doOnError(WebClientResponseException.TooManyRequests.class, tooManyRequests -> {
                    this.getLogger().warn("Too many requests reached, pausing...");
                    this.pauseInstant = Instant.now();
                    throw new RuntimeException();
                })
                .doOnError(UnsupportedMediaTypeException.class, unsupportedMediaType -> {
                    this.getLogger().warn("Too many requests reached, pausing...");
                    this.pauseInstant = Instant.now();
                    throw new RuntimeException();
                })
                .blockOptional();
    }

    // TODO make page size configurable, remember in player service during calc
    public ScoreSaberScoreListDto getPlayerScores(Long playerId, int page) throws InterruptedException {
        this.handleConnectionLimit();

        String requestUrl = String.format("%s/player/%s/scores?page=%d&sort=recent&limit=100", URL, playerId, page);
        return this.webClient.get().uri(requestUrl)
                .retrieve()
                .bodyToMono(ScoreSaberScoreListDto.class)
                .onErrorResume(WebClientResponseException.NotFound.class, notFound -> Mono.empty())
                .doOnError(WebClientResponseException.TooManyRequests.class, tooManyRequests -> {
                    this.getLogger().warn("Too many requests reached, pausing...");
                    this.pauseInstant = Instant.now();
                    throw new RuntimeException();
                })
                .doOnError(UnsupportedMediaTypeException.class, unsupportedMediaType -> {
                    this.getLogger().warn("Too many requests reached, pausing...");
                    this.pauseInstant = Instant.now();
                    throw new RuntimeException();
                })
                .block();
    }

    public byte[] loadAvatar(String avatarUrl) {
        return this.webClient.get().uri(avatarUrl)
                .accept(MediaType.IMAGE_JPEG)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    private void handleConnectionLimit() throws InterruptedException {
        this.recentRequests.stream()
                .filter(instant -> instant.isBefore(Instant.now().minusSeconds(60))).collect(Collectors.toList())
                .forEach(this.recentRequests::remove);
        int currentMaxSize = this.recentRequests.size();
        if (ScoreSaberConnector.max < currentMaxSize) {
            ScoreSaberConnector.max = currentMaxSize;
        }
        if (currentMaxSize > 390 || (this.pauseInstant.isAfter(Instant.now().minusSeconds(10)))) {
            TimeUnit.SECONDS.sleep(1);
            this.handleConnectionLimit();
            return;
        }
        this.recentRequests.add(Instant.now());
    }

    private Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    public static int getMax() {
        return max;
    }
}
