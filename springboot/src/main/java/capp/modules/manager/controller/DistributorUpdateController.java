package capp.modules.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.*;
import java.util.logging.Logger;
import jakarta.validation.*;
import org.springframework.validation.BindingResult;

import capp.modules.manager.service.DistributorUpdateService;
import capp.modules.manager.dto.DistributorDeactivateRequestDTO;
import capp.modules.manager.dto.DistributorRequestDTO;
import capp.modules.manager.dto.DistributorListXmlDTO;
import capp.modules.ResponseSuccessDTO;

@RestController
@RequestMapping("/manager/distributor")
public class DistributorUpdateController {
    private final static Logger logger = Logger.getLogger(DistributorUpdateController.class.getName());
    private final DistributorUpdateService distributorUpdateService;
    public DistributorUpdateController(DistributorUpdateService distributorUpdateService) {
        this.distributorUpdateService = distributorUpdateService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseSuccessDTO> addDistributor(@Valid @RequestBody DistributorRequestDTO distributor,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Dati di input non validi."));
        }
        try {
            boolean result = distributorUpdateService.addDistributor(distributor);
            return ResponseEntity.ok(new ResponseSuccessDTO(result, null));
        } catch (Exception e) {
            logger.info("Errore nel controller durante inserimento distributore");
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseSuccessDTO> removeDistributor(@RequestBody Map<String, String> id) {
        try {
            boolean result = distributorUpdateService.removeDistributor(id.get("id"));
            if(result){
                return ResponseEntity.ok(new ResponseSuccessDTO(true, null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Distributore non trovato."));
            }
        } catch (Exception e) {
            logger.info("Errore nel controller durante rimozione distributore");
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<ResponseSuccessDTO> activateDistributor(@RequestBody Map<String, String> id) {
        try {
            boolean result = distributorUpdateService.activateDistributor(id.get("id"));
            if(result){
                return ResponseEntity.ok(new ResponseSuccessDTO(true, null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Distributore non trovato."));
            }
        } catch (Exception e) {
            logger.info("Errore nel controller durante attivazione distributore");
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }

    @PostMapping("/deactivate")
    public ResponseEntity<ResponseSuccessDTO> deactivateDistributor(@RequestBody DistributorDeactivateRequestDTO request) {
        try {
            boolean result = distributorUpdateService.deactivateDistributor(request.id(), request.state());
            if(result){
                return ResponseEntity.ok(new ResponseSuccessDTO(true, null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Distributore non trovato."));
            }
        } catch (Exception e) {
            logger.info("Errore nel controller durante disattivazione distributore");
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }

    @GetMapping(
            value = "/search",
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public DistributorListXmlDTO searchDistributor() {
        try {
            return distributorUpdateService.getDistributorList();
        } catch (Exception e) {
            logger.info("Errore nel controller durante stampa risultati di ricerca");
            return new DistributorListXmlDTO(new ArrayList<>());
        }
    }
}

