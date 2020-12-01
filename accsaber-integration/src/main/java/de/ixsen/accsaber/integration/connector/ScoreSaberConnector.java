package de.ixsen.accsaber.integration.connector;

import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberPlayerDto;
import de.ixsen.accsaber.integration.model.scoresaber.ScoreSaberScoreListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

    public static final String URL = "https://new.scoresaber.com/api";
    private final WebClient webClient;

    private final Set<Instant> recentRequests;
    private Instant pauseInstant = Instant.MIN;

    @Autowired
    public ScoreSaberConnector(RestTemplateBuilder restTemplateBuilder) {
        this.webClient = WebClient.create();
        this.recentRequests = ConcurrentHashMap.newKeySet();
    }

    public Optional<ScoreSaberPlayerDto> getPlayerData(String playerId) throws InterruptedException {
        this.handleConnectionLimit();

        String requestUrl = URL + "/player/" + playerId + "/full";

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

    public ScoreSaberScoreListDto getPlayerScores(String playerId, int page) throws InterruptedException {
        this.handleConnectionLimit();

        String requestUrl = URL + "/player/" + playerId + "/scores/recent/" + page;
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

    private void handleConnectionLimit() throws InterruptedException {
        this.recentRequests.removeAll(this.recentRequests.stream().filter(instant -> instant.isBefore(Instant.now().minusSeconds(60))).collect(Collectors.toList()));
        if (this.recentRequests.size() > 70 || (this.pauseInstant.isAfter(Instant.now().minusSeconds(30)))) {
            TimeUnit.SECONDS.sleep(1);
            this.handleConnectionLimit();
            return;
        }
        this.recentRequests.add(Instant.now());
    }

    private Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
