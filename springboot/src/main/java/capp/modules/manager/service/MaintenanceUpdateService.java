package capp.modules.manager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

import capp.domain.entity.Credential;
import capp.domain.entity.Maintenance;
import capp.domain.entity.Role;
import capp.domain.repository.CredentialRepository;
import capp.domain.repository.MaintenanceRepository;
import capp.modules.manager.dto.MaintenanceRequestDTO;
import capp.modules.manager.dto.MaintenanceListXmlDTO;
import capp.modules.manager.dto.MaintenanceXmlDTO;

@Service
public class MaintenanceUpdateService {
    private final MaintenanceRepository maintenanceRepository;
    private final CredentialRepository credentialRepository;
    public MaintenanceUpdateService(MaintenanceRepository maintenanceRepository, CredentialRepository credentialRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.credentialRepository = credentialRepository;
    }

    @Transactional
    public boolean addMaintenance(MaintenanceRequestDTO maintenance) {
        Maintenance newMaintenance = new Maintenance(maintenance.getName(), maintenance.getLocation(), maintenance.getDate());
        maintenanceRepository.save(newMaintenance);
        Credential newCredential = new Credential(String.valueOf(newMaintenance.getId()), Role.maintenance, maintenance.getPassword());
        credentialRepository.save(newCredential);
        return true;
    }

    @Transactional
    public boolean removeMaintenance(String id) {
        Long idMaintainer = Long.parseLong(id);
        Optional<Maintenance> find= maintenanceRepository.findById(idMaintainer);
        if(find.isPresent()){
            maintenanceRepository.delete(find.get());
            credentialRepository.removeByIdAndRole(id, Role.maintenance);
            return true;
        }
        return false;
    }

    public MaintenanceListXmlDTO getMaintenanceList() {
        List<Maintenance> list = maintenanceRepository.findAll();
        List<MaintenanceXmlDTO> dtoList = list.stream()
                .map(m -> new MaintenanceXmlDTO(
                        m.getId(),
                        m.getName(),
                        m.getLocation(),
                        m.getDate()
                ))
                .collect(Collectors.toList());
        return new MaintenanceListXmlDTO(dtoList);
    }

}
