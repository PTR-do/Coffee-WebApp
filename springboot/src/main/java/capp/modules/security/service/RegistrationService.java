package capp.modules.security.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import capp.domain.entity.Consumer;
import capp.domain.entity.Credential;
import capp.domain.entity.Role;
import capp.domain.repository.ConsumerRepository;
import capp.domain.repository.CredentialRepository;
import capp.modules.security.dto.RegistrationRequestDTO;

@Service
public class RegistrationService {
    private final ConsumerRepository consumerRepository;
    private final CredentialRepository credentialRepository;
    public RegistrationService(ConsumerRepository consumerRepository, CredentialRepository credentialRepository) {
        this.consumerRepository = consumerRepository;
        this.credentialRepository = credentialRepository;
    }

    @Transactional
    public String registerUser(RegistrationRequestDTO request) {
        if (!request.password().equals(request.confirmPassword())) {
            return "Le password non coincidono.";
        }
        if (request.username().isEmpty() || consumerRepository.existsById(request.username())) {
            return "Nome utente già esistente.";
        }
        if (consumerRepository.existsByEmail(request.email())) {
            return "Email già registrata.";
        }
        Consumer newConsumer = new Consumer(request.username(), request.email(), 0.00);
        consumerRepository.save(newConsumer);
        Credential newCredential = new Credential(request.username(), Role.user, request.password());
        credentialRepository.save(newCredential);
        return "success";
    }
}
