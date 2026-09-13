package capp.modules.synchronization.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

import capp.domain.repository.DistributorRepository;
import capp.domain.entity.Distributor;
import capp.domain.entity.State;
import capp.modules.synchronization.dto.*;

@Service
public class SynchronizationService {
    private final DistributorRepository distributorRepository;
    public SynchronizationService(DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
    }

    @Transactional(readOnly = true)
    public DistributorListDTO findDistributorsToNotify() {
        List<DistributorDTO> dtoList = distributorRepository.findByNotifyFalse()
                        .stream()
                        .map(d -> new DistributorDTO(
                                d.getId(),
                                d.getState(),
                                d.getLocation(),
                                d.getLat(),
                                d.getLng()
                        ))
                        .toList();
        return new DistributorListDTO(dtoList);
    }

    @Transactional
    public void synchronization(ResponseJakartaDTO response, DistributorListDTO distributorList) {
        if (response.success()) {
            List<Long> toDelete = new ArrayList<>();
            List<Long> notified = new ArrayList<>();
            for (DistributorDTO dto : distributorList.distributors()) {
                if (dto.state() == State.rimosso) {
                    toDelete.add(dto.id());
                } else {
                    notified.add(dto.id());
                }
            }
            if (!toDelete.isEmpty()) {
                distributorRepository.deleteAllById(toDelete);
            }
            if (!notified.isEmpty()) {
                List<Distributor> distributorsNotified = distributorRepository.findAllById(notified);
                distributorsNotified.forEach(d -> d.setNotify(true));
                distributorRepository.saveAll(distributorsNotified);
            }
        }
    }
}

