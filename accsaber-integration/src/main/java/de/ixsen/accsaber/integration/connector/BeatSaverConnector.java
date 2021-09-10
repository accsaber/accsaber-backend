package de.ixsen.accsaber.integration.connector;

import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverScoreInfo;
import de.ixsen.accsaber.integration.model.beatsaver.BeatSaverSongInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.function.Tuple2;

@Service
public class BeatSaverConnector {

    public static final String URL = "https://api.beatsaver.com";
    public static final Logger logger = LoggerFactory.getLogger(BeatSaverConnector.class);
    private final WebClient webClient;


    @Autowired
    public BeatSaverConnector() {
        this.webClient = WebClient.create();
    }

    public BeatSaverSongInfo getMapInfoByKey(String key) {

        String requestUrl = URL + "/maps/id/" + key;

        logger.info("RequestUrl {}", requestUrl);

        return this.webClient.get().uri(requestUrl)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(BeatSaverSongInfo.class))
                .block();
    }

    public BeatSaverSongInfo getMapInfoByHash(String hash) {

        String requestUrl = URL + "/maps/hash/" + hash;
        return this.webClient.get().uri(requestUrl)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(BeatSaverSongInfo.class))
                .elapsed()  // map the stream's time into our streams data
                .doOnNext(tuple -> logger.trace("Getting BeatSaver info took {}ms", tuple.getT1()))
                .map(Tuple2::getT2)
                .block();
    }

    public long getScoreSaberId(String hash, int diff) {

        String requestUrl = String.format("https://beatsaver.com/api/scores/%s/1?difficulty=%d&gameMode=0", hash, diff);

        return this.webClient.get().uri(requestUrl)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(BeatSaverScoreInfo.class))
                .elapsed()  // map the stream's time into our streams data
                .doOnNext(tuple -> logger.trace("Getting BeatSaver info took {}ms", tuple.getT1()))
                .map(Tuple2::getT2)
                .map(BeatSaverScoreInfo::getUid)
                .block();
    }
}
