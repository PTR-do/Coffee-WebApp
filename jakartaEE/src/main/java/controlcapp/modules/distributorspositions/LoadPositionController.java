package controlcapp.modules.distributorspositions;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.*;

@Path("/loadPosition")
public class LoadPositionController {
    private static final Logger logger = Logger.getLogger(LoadPositionController.class.getName());
    @Inject
    private LoadPositionService loadPositionService;

    public LoadPositionController(){}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response loadPosition() {
        try {
            List<PointDTO> points = loadPositionService.getAllPositions();
            LoadPositionResponseDTO response = new LoadPositionResponseDTO(true, points);
            return Response.ok(response).build();
        } catch (Exception e) {
            logger.info("Errore durante il caricamento delle posizioni");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new LoadPositionResponseDTO(false, null))
                    .build();
        }
    }
}