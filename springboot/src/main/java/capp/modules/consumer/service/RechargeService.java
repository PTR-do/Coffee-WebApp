package capp.modules.consumer.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import java.util.*;

import capp.domain.entity.Consumer;
import capp.domain.repository.ConsumerRepository;
import capp.modules.consumer.dto.RechargeRequestDTO;

@Service
public class RechargeService {
    private final ConsumerRepository consumerRepository;
    public RechargeService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    @Transactional
    public boolean rechargeCredit(@Valid RechargeRequestDTO request){
        Optional<Consumer> c = consumerRepository.findById(request.username());
        if(c.isEmpty()){
            return false;
        }
        Consumer consumer = c.get();
        try {
            double recharge = Double.parseDouble(request.credit());
            consumer.setCredit(consumer.getCredit() + recharge);
            consumerRepository.save(consumer);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
