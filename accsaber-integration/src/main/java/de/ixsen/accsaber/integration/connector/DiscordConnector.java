package de.ixsen.accsaber.integration.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class DiscordConnector {

    private final WebClient webClient;
    private final String url;
    private final long clientId;
    private final String clientSecret;


    @Autowired
    public DiscordConnector(RestTemplateBuilder restTemplateBuilder,
                            @Value("${accsaber.discord.auth-url}") String url,
                            @Value("${accsaber.discord.client-id}") long clientId,
                            @Value("${accsaber.discord.client-secret}") String clientSecret) {
        this.webClient = WebClient.create();
        this.url = url;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public Optional<String> getPlayerData(String code, String redirectUri) {
        String requestUrl = this.url + "/token";
        String body = "client_id=" + this.clientId +
                "&client_secret=" + this.clientSecret +
                "&grant_type=authorization_code&" +
                "redirect_uri=" + redirectUri +
                "&code=" + code;

        return this.webClient.post().uri(requestUrl)
                .body(Mono.just(body), String.class)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional();
    }

    public Optional<String> getAuthorizationInformation(String bearerToken) {
        String requestUrl = this.url + "/@me";

        return this.webClient.get().uri(requestUrl)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .retrieve()
                .bodyToMono(String.class)
                .blockOptional();

    }

    private Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }
}
