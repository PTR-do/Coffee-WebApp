package capp.modules.consumer.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

import capp.domain.entity.*;
import capp.domain.repository.ConnectionRepository;
import capp.domain.repository.ConsumerRepository;
import capp.domain.repository.DistributorRepository;
import capp.modules.consumer.dto.ConnectRequestDTO;
import capp.modules.consumer.dto.DisconnectRequestDTO;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final DistributorRepository distributorRepository;
    private final ConsumerRepository consumerRepository;
    public ConnectionService(ConnectionRepository connectionRepository,
                             DistributorRepository distributorRepository,
                             ConsumerRepository consumerRepository) {
        this.connectionRepository = connectionRepository;
        this.distributorRepository = distributorRepository;
        this.consumerRepository = consumerRepository;
    }

    @Transactional
    public boolean requestConnection(ConnectRequestDTO request){
        long idDistributor;
        try {
            idDistributor = Long.parseLong(request.distributorId());
        } catch (NumberFormatException e){
            return false;
        }
        Optional<Distributor> state = distributorRepository.findById(idDistributor);
        if(state.isEmpty() || !state.get().getState().equals(State.attivo)){
            return false;
        }
        Optional<Consumer> consumer = consumerRepository.findById(request.username());
        if(consumer.isEmpty()){
            return false;
        }
        Optional<Connection> connection = connectionRepository.findById(idDistributor);
        if(connection.isEmpty()){
            return false;
        }
        Connection c = connection.get();
        LocalDateTime now = LocalDateTime.now();
        if(connectionRepository.isExpired(idDistributor, now) && c.getCode().equals(request.distributorCode())){
            c.setUsername(request.username());
            c.setExpires(now);
            connectionRepository.save(c);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean requestDisconnection(DisconnectRequestDTO request){
        long idDistributor;
        try {
            idDistributor = Long.parseLong(request.distributorId());
        } catch (NumberFormatException e){
            return false;
        }
        Optional<Connection> connection = connectionRepository.findById(idDistributor);
        if(connection.isEmpty()){
            return false;
        }
        Connection c = connection.get();
        if(c.getUsername().equals(request.username())){
            c.setExpires(LocalDateTime.of(1970, 1, 1, 0, 0));
            c.setUsername("");
            connectionRepository.save(c);
            return true;
        }
        return false;
    }

}