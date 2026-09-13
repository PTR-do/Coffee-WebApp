package controlcapp.modules.synchronization.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.logging.Logger;

import controlcapp.modules.synchronization.service.IndexSynchronizationService;
import controlcapp.modules.synchronization.dto.DistributorListDTO;
import controlcapp.modules.synchronization.dto.ResponseIndexDTO;

@Path("/synchronization")
public class IndexSynchronizationController {
    private final static Logger logger = Logger.getLogger(IndexSynchronizationController.class.getName());
    private IndexSynchronizationService synchronizationService;

    @Inject
    public IndexSynchronizationController(IndexSynchronizationService synchronizationService) {
        this.synchronizationService = synchronizationService;
    }
    public IndexSynchronizationController(){}

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseIndexDTO confirmIndex(DistributorListDTO distributorList) {
        try {
            boolean result = synchronizationService.receiveList(distributorList);
            return new ResponseIndexDTO(result);
        } catch (Exception e) {
            logger.info("Errore durante la sincronizzazione Jakarta: " + e.getMessage());
            return new ResponseIndexDTO(false);
        }
    }
}