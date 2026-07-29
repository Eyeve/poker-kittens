package io.github.eyeve.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "lobby")
@Data
public class Lobby {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "lobby")
    private List<User> members;

    private boolean isRanked;

    private LocalDateTime startTime; // ? Instant ZonedDateTime
    // TODO LobbyManager startGame()
}
