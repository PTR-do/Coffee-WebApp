package capp.modules.maintenance.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

import capp.modules.maintenance.service.*;
import capp.modules.maintenance.dto.DistributorStateListXmlDTO;
import capp.modules.ResponseSuccessDTO;

@RestController
@RequestMapping("/maintenance/distributorState")
public class DistributorStateController {
    private static final Logger logger = Logger.getLogger(DistributorStateController.class.getName());
    private final StateRequestService stateRequestService;
    private final UpdateStateService updateStateService;
    public DistributorStateController(StateRequestService stateRequestService,
                                      UpdateStateService updateStateService) {
        this.stateRequestService = stateRequestService;
        this.updateStateService = updateStateService;
    }

    @PostMapping(
            value = "/requestState",
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public DistributorStateListXmlDTO sendState(@RequestBody Map<String, String> id) {
        try{
            return stateRequestService.sendDistributorStateList(id.get("id"));
        } catch (Exception e) {
            logger.info("Errore durante la generazione della lista stato distributore.");
            return new DistributorStateListXmlDTO(new ArrayList<>());
        }
    }

    @PostMapping("/updateState")
    public ResponseEntity<ResponseSuccessDTO> updateDistributorState(@RequestBody String xml) {
        try {
            boolean result = updateStateService.updateDistributorState(xml);
            if (result) {
                return ResponseEntity.ok(new ResponseSuccessDTO(true, null));
            } else {
                return ResponseEntity.badRequest()
                        .body(new ResponseSuccessDTO(false, "Errore durante l'elaborazione, controllare i dati forniti."));
            }
        } catch (Exception e) {
            logger.info("Errore critico durante l'aggiornamento dello stato."+ e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }
}
