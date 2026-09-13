package capp.modules.security.config;

import capp.domain.entity.Credential;
import capp.domain.entity.Role;
import capp.domain.repository.CredentialRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EspressoUserDetailsService implements UserDetailsService {
    private final CredentialRepository credentialRepository;
    private final HttpServletRequest request;   // richiesta HTTP per leggere parametri extra oltre allo username
    public EspressoUserDetailsService(CredentialRepository credentialRepository, HttpServletRequest request) {
        this.credentialRepository = credentialRepository;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        String roleParam = request.getParameter("role");
        if (roleParam == null) {
            throw new UsernameNotFoundException("Ruolo mancante nella richiesta di login.");
        }
        Role role;
        try {
            role = Role.valueOf(roleParam.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new UsernameNotFoundException("Ruolo non valido: " + roleParam);
        }
        Credential credential = credentialRepository.findByIdAndRole(id, role)
                .orElseThrow(() -> new UsernameNotFoundException("Credenziali errate per l'ID " + id + " con ruolo " + roleParam));
        return User.builder()
                .username(credential.getId())
                .password(credential.getPassword())
                .roles(credential.getRole().name().toUpperCase())
                .build();
    }
}
