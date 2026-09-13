package controlcapp.modules.distributorspositions;

import controlcapp.domain.DistributorCollectionRepository;
import controlcapp.domain.DistributorDocument;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LoadPositionService {
    private DistributorCollectionRepository repository;
    @Inject
    public LoadPositionService(DistributorCollectionRepository repository) {
        this.repository = repository;
    }
    protected LoadPositionService() {
    }

    public List<PointDTO> getAllPositions() {
        List<DistributorDocument> docs = repository.findAll();
        if (docs.isEmpty()) {
            return new ArrayList<>();
        }
        List<PointDTO> points = new ArrayList<>();
        for (DistributorDocument d : docs) {
            if (d.getCoord() != null) {
                points.add(new PointDTO(
                        d.getCoord().getLat(),
                        d.getCoord().getLng(),
                        d.getLocation(),
                        d.getState().name()
                ));
            }
        }
        return points;
    }
}