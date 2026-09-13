package controlcapp.modules.synchronization.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import controlcapp.domain.*;
import controlcapp.modules.synchronization.dto.DistributorDTO;
import controlcapp.modules.synchronization.dto.DistributorListDTO;

@ApplicationScoped
public class IndexSynchronizationService {
    private DistributorCollectionRepository repository;

    @Inject
    public IndexSynchronizationService(DistributorCollectionRepository repository) {
        this.repository = repository;
    }
    public IndexSynchronizationService(){
    }

    public boolean receiveList(DistributorListDTO distributorListDTO) {
        List<DistributorDTO> distributors = distributorListDTO.distributors();
        if (distributors == null || distributors.isEmpty()) {
            return false;
        }
        List<DistributorDocument> toSave = new ArrayList<>();
        List<DistributorDocument> toDelete = new ArrayList<>();
        List<Long> ids = distributors.stream().map(DistributorDTO::getId).toList();
        List<DistributorDocument> existingDocuments = repository.findByIds(ids);
        Map<Long, DistributorDocument> existingMap = existingDocuments.stream()
                .collect(Collectors.toMap(DistributorDocument::getId, d -> d));
        for (DistributorDTO dto : distributors) {
            DistributorDocument document = existingMap.get(dto.getId());
            if (document == null) {
                DistributorDocument.Coord coord = new DistributorDocument.Coord();
                coord.setLat(dto.getLat());
                coord.setLng(dto.getLng());
                document = new DistributorDocument(
                        dto.getId(),
                        dto.getState(),
                        null,
                        dto.getLocation(),
                        coord
                );
            } else {
                document.setState(dto.getState());
                if(document.getState()==State.rimosso){
                    toDelete.add(document);
                    continue;
                }
            }
            toSave.add(document);
        }
        if(!toSave.isEmpty()) repository.saveAll(toSave);
        if(!toDelete.isEmpty()) repository.deleteAll(toDelete);
        return true;
    }
}
