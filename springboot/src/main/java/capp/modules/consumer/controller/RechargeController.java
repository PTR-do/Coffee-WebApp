package capp.modules.consumer.controller;

import java.util.logging.Logger;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import capp.modules.consumer.service.RechargeService;
import capp.modules.consumer.dto.RechargeRequestDTO;
import capp.modules.ResponseSuccessDTO;

@RestController
@RequestMapping("/consumer/recharge")
public class RechargeController {
    private final static Logger logger = Logger.getLogger(RechargeController.class.getName());
    private final RechargeService rechargeService;
    public RechargeController(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    @PostMapping
    public ResponseEntity<ResponseSuccessDTO> rechargeCredit(@Valid @RequestBody RechargeRequestDTO request,
                                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Dati di input non validi."));
        }
        try {
            boolean result = rechargeService.rechargeCredit(request);
            if(result){
                return ResponseEntity.ok(new ResponseSuccessDTO(true, null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Errore nei dati inseriti."));
            }
        } catch (Exception e) {
            logger.info("Errore nel controller durante il servizio di ricarica");
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }
}
