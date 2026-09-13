package controlcapp.modules.synchronization.client;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import controlcapp.modules.synchronization.service.ReportStateService;
import controlcapp.modules.synchronization.dto.DistributorStatusListDTO;

@ApplicationScoped
public class ReportClient {
    private Client client;
    private String reportUrl;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ReportStateService reportService;
    private final static Logger logger = Logger.getLogger(ReportClient.class.getName());

    @Inject
    public ReportClient(ReportStateService reportService,
                        @ConfigProperty(name = "springboot.service.url") String baseUrl) {
        this.reportService = reportService;
        this.client = ClientBuilder.newClient();
        this.reportUrl = baseUrl + "/report";
    }
    public ReportClient(){}

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        scheduleReportTask();
    }
    @PreDestroy
    private void shutdown() {
        scheduler.shutdown();
        client.close();
    }

    private void sendReport() {
        try {
            DistributorStatusListDTO dto = reportService.prepareDistributorStates();
            if(dto == null) return;
            client.target(reportUrl)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(dto, MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            logger.info("Errore durante l'invio del report" + e.getMessage());
        }
    }

    private void scheduleReportTask() {
        long initialDelay = delayTime();
        scheduler.scheduleAtFixedRate(this::sendReport, initialDelay, 60, TimeUnit.MINUTES);
    }
    private long delayTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withMinute(5).withSecond(0).withNano(0);
        if (!now.isBefore(nextRun)) {
            nextRun = nextRun.plusHours(1);
        }
        return ChronoUnit.MINUTES.between(now, nextRun);
    }
}
