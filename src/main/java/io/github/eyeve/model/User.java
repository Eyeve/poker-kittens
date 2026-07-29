package io.github.eyeve.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private Double rating;
    private Double rd;
    private Double volatility;
    private LocalDateTime lastRatingUpdate;

    @ManyToOne
    @JoinColumn(name = "lobby_id")
    private Lobby lobby;
}
