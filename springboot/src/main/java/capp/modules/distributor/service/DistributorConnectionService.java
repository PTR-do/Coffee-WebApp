package capp.modules.distributor.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import capp.domain.entity.Connection;
import capp.domain.entity.Consumer;
import capp.domain.repository.ConnectionRepository;
import capp.domain.repository.ConsumerRepository;
import capp.modules.distributor.dto.*;

@Service
public class DistributorConnectionService {
    private final ConnectionRepository connectionRepository;
    private final ConsumerRepository consumerRepository;
    public DistributorConnectionService(ConnectionRepository connectionRepository,
                                        ConsumerRepository consumerRepository) {
        this.connectionRepository = connectionRepository;
        this.consumerRepository = consumerRepository;
    }

    @Transactional
    public boolean codeSaving(CodeRequestDTO request) {
        long idDistributor;
        try {
            idDistributor = Long.parseLong(request.distributorId());
        } catch (NumberFormatException e){
            return false;
        }
        Optional<Connection> connection = connectionRepository.findById(idDistributor);
        LocalDateTime now = LocalDateTime.now();
        if (connection.isPresent() && connectionRepository.isExpired(idDistributor, now)) {
            Connection c = connection.get();
            c.setCode(request.distributorCode());
            c.setUsername("");
            connectionRepository.save(c);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public ConnectionDTO callResponse(String id){
        long idDistributor;
        try {
            idDistributor = Long.parseLong(id);
        } catch (NumberFormatException e){
            return new ConnectionDTO(false, "", 0.00);
        }
        Optional<Connection> connection = connectionRepository.findById(idDistributor);
        if (connection.isEmpty()) {
            throw new jakarta.persistence.EntityNotFoundException();
        }
        Connection c = connection.get();
        double credit = 0.00;
        LocalDateTime now = LocalDateTime.now();
        if (!connectionRepository.isExpired(idDistributor, now) && !c.getUsername().isEmpty()) {
            Optional<Consumer> user = consumerRepository.findById(c.getUsername());
            if (user.isPresent()) {
                credit = user.get().getCredit();
            }
            return new ConnectionDTO(true, c.getUsername(), credit);
        } else {
            return new ConnectionDTO(false, "", credit);
        }
    }

}
