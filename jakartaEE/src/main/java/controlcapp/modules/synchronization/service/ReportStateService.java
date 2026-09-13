package controlcapp.modules.synchronization.service;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import controlcapp.domain.*;
import controlcapp.modules.synchronization.dto.DistributorStatusDTO;
import controlcapp.modules.synchronization.dto.DistributorStatusListDTO;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ReportStateService {
    private DistributorCollectionRepository repository;
    private final static Logger logger = Logger.getLogger(ReportStateService.class.getName());
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "HealthCheckThread");
        t.setDaemon(true);
        return t;
    });

    @Inject
    public ReportStateService(DistributorCollectionRepository repository) {
        this.repository = repository;
    }
    public ReportStateService(){
    }

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        scheduler.scheduleAtFixedRate(this::checkDistributorHealth, 0, 1, TimeUnit.HOURS);
    }
    @Transactional
    protected void checkDistributorHealth() {
        try {
            List<DistributorDocument> distributors = repository.findAll();
            LocalDateTime limit = LocalDateTime.now().minusHours(3);
            for (DistributorDocument d : distributors) {
                if (d.getState()==State.attivo &&
                        d.getTime() != null && d.getTime().isBefore(limit)) {
                    d.setState(State.guasto);
                    repository.save(d);
                }
            }
        } catch (Exception e) {
            logger.info("Errore durante il controllo periodico di monitoraggio "+e.getMessage());
        }
    }
    @PreDestroy
    private void shutdown() {
        scheduler.shutdown();
    }

    @Transactional
    public DistributorStatusListDTO prepareDistributorStates() {
        List<DistributorDocument> distributors = repository.findAll();
        if (distributors.isEmpty()) {
            return null;
        }
        List<DistributorStatusDTO> statusList = new ArrayList<>();
        for (DistributorDocument d : distributors) {
            statusList.add(new DistributorStatusDTO(d.getId().toString(), d.getState()));
        }
        return new DistributorStatusListDTO(statusList);
    }

}