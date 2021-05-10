package de.ixsen.accsaber.api.controllers;

import de.ixsen.accsaber.api.dtos.login.LoginDto;
import de.ixsen.accsaber.business.HasLogger;
import de.ixsen.accsaber.integration.connector.DiscordConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("login")
public class LoginController implements HasLogger {

    private final DiscordConnector discordConnector;

    @Autowired
    public LoginController(DiscordConnector discordConnector) {
        this.discordConnector = discordConnector;
    }

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        Optional<String> playerData = this.discordConnector.getPlayerData(loginDto.getCode(), loginDto.getRedirectUri());
        return ResponseEntity.ok(playerData.get());
    }

    @GetMapping
    public ResponseEntity<String> getLoginInformation(@RequestHeader String authorization) {
        this.getLogger().info(authorization);
        return ResponseEntity.ok(this.discordConnector.getAuthorizationInformation(authorization).get());
    }
}
