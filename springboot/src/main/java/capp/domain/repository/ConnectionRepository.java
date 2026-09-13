package capp.domain.repository;

import capp.domain.entity.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ConnectionRepository extends JpaRepository<Connection, Long> {

    void removeByIdDistributor(Long idDistributor);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
            "FROM connection " +
            "WHERE idDistributor = :id AND expires < DATE_SUB(:currentTime, INTERVAL 1 MINUTE)",
            nativeQuery = true)
    int checkExpiredInternal(@Param("id") Long idDistributor, @Param("currentTime") LocalDateTime currentTime);

    default boolean isExpired(Long idDistributor, LocalDateTime currentTime) {
        return checkExpiredInternal(idDistributor, currentTime) > 0;
    }
}