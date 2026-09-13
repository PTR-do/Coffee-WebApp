package capp.modules.consumer.controller;

import java.util.logging.Logger;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import capp.modules.consumer.service.ConnectionService;
import capp.modules.consumer.dto.ConnectRequestDTO;
import capp.modules.consumer.dto.DisconnectRequestDTO;
import capp.modules.ResponseSuccessDTO;

@RestController
@RequestMapping("/consumer")
public class ConnectionController {
    private final static Logger logger = Logger.getLogger(ConnectionController.class.getName());
    private final ConnectionService connectionService;
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping("/connect")
    public ResponseEntity<ResponseSuccessDTO> requestConnection(@Valid @RequestBody ConnectRequestDTO request,
                                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Dati di input non validi."));
        }
        try {
            boolean result = connectionService.requestConnection(request);
            if(result){
                return ResponseEntity.ok(new ResponseSuccessDTO(true, null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Errore nei dati inseriti."));
            }
        } catch (Exception e) {
            logger.info("Errore nel controller durante accettazione connessione"+ e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }

    @PostMapping("/disconnect")
    public ResponseEntity<ResponseSuccessDTO> requestDisconnection(@Valid @RequestBody DisconnectRequestDTO request,
                                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Dati di input non validi."));
        }
        try {
            boolean result = connectionService.requestDisconnection(request);
            if(result){
                return ResponseEntity.ok(new ResponseSuccessDTO(true, null));
            } else {
                return ResponseEntity.badRequest().body(new ResponseSuccessDTO(false, "Errore nei dati inseriti."));
            }
        } catch (Exception e) {
            logger.info("Errore nel controller durante accettazione disconnessione");
            return ResponseEntity.internalServerError().body(new ResponseSuccessDTO(false, "Errore server"));
        }
    }
}
