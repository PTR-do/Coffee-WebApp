package capp.domain.repository;

import capp.domain.entity.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DistributorRepository extends JpaRepository<Distributor, Long> {

    // Recupera solo i distributori da notificare
    List<Distributor> findByNotifyFalse();
}
