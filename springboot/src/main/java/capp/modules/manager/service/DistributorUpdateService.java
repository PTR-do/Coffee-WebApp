package capp.modules.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import capp.domain.entity.*;
import capp.domain.repository.ConnectionRepository;
import capp.domain.repository.CredentialRepository;
import capp.domain.repository.DistributorRepository;
import capp.modules.manager.dto.DistributorRequestDTO;
import capp.modules.manager.dto.DistributorListXmlDTO;
import capp.modules.manager.dto.DistributorXmlDTO;

@Service
public class DistributorUpdateService {
    private final DistributorRepository distributorRepository;
    private final CredentialRepository credentialRepository;
    private final ConnectionRepository connectionRepository;
    public DistributorUpdateService(CredentialRepository credentialRepository,
                                    ConnectionRepository connectionRepository,
                                    DistributorRepository distributorRepository) {
        this.distributorRepository = distributorRepository;
        this.credentialRepository = credentialRepository;
        this.connectionRepository = connectionRepository;
    }

    @Transactional
    public boolean addDistributor(DistributorRequestDTO distributor) {
        Distributor newDistributor = new Distributor(
                distributor.getLocation(),
                distributor.getDate(),
                distributor.getLat(),
                distributor.getLng(),
                false,
                distributor.getState()
        );
        distributorRepository.save(newDistributor);
        Credential newCredential = new Credential(String.valueOf(newDistributor.getId()), Role.distributor, "UniqueDistributorPassword");
        credentialRepository.save(newCredential);
        Connection newConnection = new Connection(newDistributor.getId(), "", "", LocalDateTime.of(1970, 1, 1, 0, 0));
        connectionRepository.save(newConnection);
        return true;
    }

    @Transactional
    public boolean removeDistributor(String id) {
        Long idDistributor = Long.parseLong(id);
        Optional<Distributor> find = distributorRepository.findById(idDistributor);
        if (find.isPresent() && find.get().getState()!=State.rimosso) {
            find.get().setState(State.rimosso);  //not removed from table distributor
            find.get().setNotify(false);
            distributorRepository.save(find.get());
            credentialRepository.removeByIdAndRole(id, Role.distributor);
            connectionRepository.removeByIdDistributor(idDistributor);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean activateDistributor(String id){
        Long idDistributor = Long.parseLong(id);
        Optional<Distributor> opt = distributorRepository.findById(idDistributor);
        if (opt.isEmpty() || opt.get().getState()==State.rimosso) {
            return false;
        }
        Distributor distributor = opt.get();
        if (distributor.getState() == State.attivo) {
            return true;
        }
        distributor.setState(State.attivo);
        distributor.setNotify(false);
        distributorRepository.save(distributor);
        return true;
    }

    @Transactional
    public boolean deactivateDistributor(String id, String status){
        Long idDistributor = Long.parseLong(id);
        Optional<Distributor> opt = distributorRepository.findById(idDistributor);
        if (opt.isEmpty() || opt.get().getState()==State.rimosso) {
            return false;
        }
        Distributor distributor = opt.get();
        State newState = State.valueOf(status);
        if (distributor.getState() == newState) {
            return true;
        }
        distributor.setState(newState);
        distributor.setNotify(false);
        distributorRepository.save(distributor);
        return true;
    }

    @Transactional(readOnly = true)
    public DistributorListXmlDTO getDistributorList() {
        List<Distributor> list = distributorRepository.findAll();
        List<DistributorXmlDTO> dtoList = list.stream()
                .map(d -> new DistributorXmlDTO(
                        d.getId(),
                        d.getLocation(),
                        d.getDate(),
                        d.getState()
                ))
                .collect(Collectors.toList());
        return new DistributorListXmlDTO(dtoList);
    }
}
