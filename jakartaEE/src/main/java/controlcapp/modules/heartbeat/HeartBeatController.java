package controlcapp.modules.heartbeat;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;
import java.util.logging.Logger;

@Path("/heartbeat")
public class HeartBeatController {
    private HeartBeatService heartBeatService;
    private final static Logger logger = Logger.getLogger(HeartBeatController.class.getName());

    @Inject
    public HeartBeatController(HeartBeatService heartBeatService) {
        this.heartBeatService = heartBeatService;
    }
    public HeartBeatController(){}

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void heartbeatControl(Map<String,String> id) {
        try {
            heartBeatService.reportHeartBeat(id.get("id"));
        } catch (Exception e) {
            logger.info("Errore heartbeat id: "+id.get("id")+": "+e.getMessage());
        }
    }
}
