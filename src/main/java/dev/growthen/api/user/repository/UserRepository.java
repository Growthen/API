package dev.growthen.api.user.repository;

import dev.growthen.api.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsernameAndIsDeletedFalse(String username);

    Optional<User> findByIdAndIsDeletedFalse(UUID id);

    boolean existsByUsernameAndIsDeletedFalse(String username);

    boolean existsByEmailAndIsDeletedFalse(String email);
}
