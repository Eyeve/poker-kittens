package io.github.eyeve.service;

import io.github.eyeve.model.User;
import io.github.eyeve.repository.LobbyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final LobbyRepository lobbyRepository;

    void joinLobby(User user) {

    }

    /**
     * 123
     */
    void createCustomLobby(User owner) {

    }



    void createTournament() {

    }

    @Scheduled(cron = "0 30 22 * * SUN")
    void startTournament() {

        createTournament();
    }



    private void createLobby() {

    }

    private void startLobby() {

    }

}
