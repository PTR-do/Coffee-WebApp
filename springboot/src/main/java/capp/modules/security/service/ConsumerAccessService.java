package capp.modules.security.service;

import org.springframework.stereotype.Service;

import capp.domain.repository.ConsumerRepository;

@Service
public class ConsumerAccessService {
    private final ConsumerRepository consumerRepository;

    public ConsumerAccessService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    public String getConsumerCredit(String username) {
        return consumerRepository.findById(username)
                .map(consumer -> String.format("%.2f", consumer.getCredit()))
                .orElse("0.00");
    }
}