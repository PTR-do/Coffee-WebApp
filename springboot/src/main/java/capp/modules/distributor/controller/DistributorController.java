package capp.modules.distributor.controller;

import capp.modules.distributor.service.DrinkVerificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.logging.Logger;

import capp.modules.distributor.dto.CodeRequestDTO;
import capp.modules.distributor.dto.ConnectionDTO;
import capp.modules.distributor.dto.SelectionRequestDTO;
import capp.modules.distributor.service.DistributorConnectionService;
import capp.modules.ResponseSuccessDTO;

@RestController
@RequestMapping("/distributor/machine")
public class DistributorController {
    private final static Logger logger = Logger.getLogger(DistributorController.class.getName());
    private final DistributorConnectionService distributorConnectionService;
    private final DrinkVerificationService drinkVerificationService;
    public DistributorController(DistributorConnectionService distributorConnectionService,
                                 DrinkVerificationService drinkVerificationService) {
        this.distributorConnectionService = distributorConnectionService;
        this.drinkVerificationService = drinkVerificationService;

    }

    @PostMapping("/codeGeneration")
    public ResponseEntity<ResponseSuccessDTO> codeGeneration(@Valid @RequestBody CodeRequestDTO request,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Dati di input non validi."));
        }
        try {
            boolean result = distributorConnectionService.codeSaving(request);
            return ResponseEntity.ok(new ResponseSuccessDTO(result, null));
        } catch (Exception e) {
            logger.info("Errore nel controller durante inserimento codice"+e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }

    @PostMapping("/pollCall")
    public ResponseEntity<?> callResponse(@RequestBody Map<String, String> request) {
        String id = request.get("id");
        try {
            ConnectionDTO connectionInfo = distributorConnectionService.callResponse(id);
            return ResponseEntity.ok(connectionInfo);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Distributore non esistente."));
        } catch (Exception e) {
            logger.info("Errore nel controller durante pollCall per ID: " + id);
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server."));
        }
    }

    @PostMapping("/permission")
    public ResponseEntity<ResponseSuccessDTO> permission(@RequestBody SelectionRequestDTO request) {
        try {
            String result = drinkVerificationService.allowDrink(request);
            if(result.equals("done")){
                return ResponseEntity.ok(new ResponseSuccessDTO(true, null));
            } else if (result.equals("error supply")) {
                return ResponseEntity.ok(new ResponseSuccessDTO(false, "Scorte esaurite per la bevanda selezionata"));
            } else {
                return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Acquisto non permesso."));
            }
        } catch (Exception e) {
            logger.info("Errore nel controller durante erogazione prodotto");
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }
}
