package capp.modules.manager.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.Map;

import capp.modules.manager.service.MaintenanceUpdateService;
import capp.modules.manager.dto.MaintenanceRequestDTO;
import capp.modules.manager.dto.MaintenanceListXmlDTO;
import capp.modules.ResponseSuccessDTO;

@RestController
@RequestMapping("/manager/maintenance/")
public class MaintenanceUpdateController {
    private final static Logger logger =  Logger.getLogger(MaintenanceUpdateController.class.getName());
    private final MaintenanceUpdateService maintenanceUpdateService;
    public MaintenanceUpdateController(MaintenanceUpdateService maintenanceUpdateService) {
        this.maintenanceUpdateService = maintenanceUpdateService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseSuccessDTO> addMaintenance(@Valid @RequestBody MaintenanceRequestDTO maintenance,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Dati di input non validi."));
        }
        try {
            boolean result = maintenanceUpdateService.addMaintenance(maintenance);
            return ResponseEntity.ok(new ResponseSuccessDTO(result, null));
        } catch (Exception e) {
            logger.info("Errore nel controller durante inserimento manutentore");
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseSuccessDTO> removeMaintenance(@RequestBody Map<String, String> id) {
        try {
            boolean result = maintenanceUpdateService.removeMaintenance(id.get("id"));
            if(result){
                return ResponseEntity.ok(new ResponseSuccessDTO(true, null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Manutentore non trovato."));
            }
        } catch (Exception e) {
            logger.info("Errore nel controller durante rimozione distributore");
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }

    @GetMapping(
            value = "/search",
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public MaintenanceListXmlDTO searchMaintenance() {
        try {
            return maintenanceUpdateService.getMaintenanceList();
        } catch (Exception e) {
            logger.info("Errore nel controller durante stampa risultati di ricerca");
            return new MaintenanceListXmlDTO(new ArrayList<>());
        }
    }
}
