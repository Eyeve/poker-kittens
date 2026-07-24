package io.github.eyeve.repository;

import io.github.eyeve.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
