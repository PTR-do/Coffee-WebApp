package capp.modules.synchronization.controller;

import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import capp.modules.synchronization.dto.DistributorStatusListDTO;
import capp.modules.synchronization.service.ReportService;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final static Logger logger = Logger.getLogger(ReportController.class.getName());
    private final ReportService reportService;
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public void heartbeatState(@RequestBody DistributorStatusListDTO distributorListDTO){
        try {
            reportService.updateState(distributorListDTO);
        } catch (Exception e) {
            logger.info("Errore durante la processazione del report" + e.getMessage());
        }
    }
}
