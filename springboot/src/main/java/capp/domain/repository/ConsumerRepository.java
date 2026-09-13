package capp.domain.repository;

import capp.domain.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, String> {
    // Controllo esistenza email
    boolean existsByEmail(String email);
}