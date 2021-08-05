package de.ixsen.accsaber.integration.connector;

import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BeatSaverConnector {

    public static final String URL = "https://api.beatsaver.com";
    private final WebClient webClient;


    @Autowired
    public BeatSaverConnector() {
        this.webClient = WebClient.create();
    }

    public BeatSaverSongInfo getMapInfoByKey(String key) {

        String requestUrl = URL + "/maps/id/" + key;
        LoggerFactory.getLogger(this.getClass()).info("RequestUrl {}", requestUrl);

        return this.webClient.get().uri(requestUrl)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(BeatSaverSongInfo.class))
                .block();
    }

    public BeatSaverSongInfo getMapInfoByHash(String hash) {

        String requestUrl = URL + "/maps/hash/" + hash;
        return this.webClient.get().uri(requestUrl)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(BeatSaverSongInfo.class))
                .block();
    }

}
