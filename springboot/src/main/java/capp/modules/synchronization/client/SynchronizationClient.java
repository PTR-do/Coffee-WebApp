package capp.modules.synchronization.client;

import java.util.logging.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import capp.modules.synchronization.service.SynchronizationService;
import capp.modules.synchronization.dto.*;

@Component
public class SynchronizationClient {
    private final WebClient webClient;
    private final SynchronizationService synchronizationService;
    private final static Logger logger = Logger.getLogger(SynchronizationClient.class.getName());
    public SynchronizationClient(
            SynchronizationService synchronizationService,
            WebClient.Builder webClientBuilder) {
        this.synchronizationService = synchronizationService;
        this.webClient = webClientBuilder.build();
    }

    @Scheduled(cron = "0 0 * * * *")
    public void sendDistributorsToJakarta() {
        DistributorListDTO distributorList = synchronizationService.findDistributorsToNotify();
        if(distributorList.distributors().isEmpty()){
            return;
        }
        try {
            webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("localhost")
                            .port(8080)
                            .path("/servicejakarta/synchronization")
                            .build())
                    .bodyValue(distributorList)
                    .retrieve()
                    .bodyToMono(ResponseJakartaDTO.class)
                    .doOnSuccess(response ->
                            synchronizationService.synchronization(response, distributorList))
                    .subscribe();
        } catch (Exception e) {
            logger.info("Errore durante la sincronizzazione "+e.getMessage());
        }
    }
}
