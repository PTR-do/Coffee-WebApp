package capp.domain.repository;

import capp.domain.entity.Role;
import capp.domain.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, String> {

    // Cerca una credential per id e role
    Optional<Credential> findByIdAndRole(String id, Role role);

    // Rimuove una credential per id e role
    void removeByIdAndRole(String id, Role role);
}
