package controlcapp.modules.heartbeat;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.*;
import java.util.logging.Logger;

import controlcapp.domain.*;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class HeartBeatService {
    private DistributorCollectionRepository repository;
    private static final Logger logger = Logger.getLogger(HeartBeatService.class.getName());

    @Inject
    public HeartBeatService(DistributorCollectionRepository repository) {
        this.repository = repository;
    }
    public HeartBeatService() {
    }

    @Transactional
    public void reportHeartBeat(String id) {
        long numericId;
        try {
            numericId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            logger.info("ID ricevuto in formato non valido: " + id);
            return;
        }
        DistributorDocument d = repository.findById(numericId);
        if (d == null) {
            return;
        }
        d.setTime(LocalDateTime.now());
        repository.save(d);
    }
}
