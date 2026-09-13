package capp.modules.synchronization.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

import capp.domain.repository.DistributorRepository;
import capp.domain.entity.Distributor;
import capp.domain.entity.State;
import capp.modules.synchronization.dto.DistributorStatusListDTO;
import capp.modules.synchronization.dto.DistributorStatusDTO;

@Service
public class ReportService {
    private final DistributorRepository distributorRepository;
    public ReportService(DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
    }

    @Transactional
    public void updateState(DistributorStatusListDTO distributorListDTO) {
        if (distributorListDTO.distributors().isEmpty()) {
            return;
        }
        Map<Long, State> report = distributorListDTO.distributors()
                .stream()
                .collect(Collectors.toMap(
                        dto -> Long.parseLong(dto.id()),
                        DistributorStatusDTO::state
                ));
        List<Distributor> distributors = distributorRepository.findAllById(report.keySet());
        for (Distributor distributor : distributors) {
            State newState = report.get(distributor.getId());
            if (newState != null && distributor.getState() == State.attivo) {
                distributor.setState(newState);
            }
        }
        distributorRepository.saveAll(distributors);
    }

}
