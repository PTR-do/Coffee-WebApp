package capp.modules.security.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Set;

@Component
public class EspressoAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_DISTRIBUTOR")) {
            response.sendRedirect("/serviceSpringBoot/distributor/access");
        } else if (roles.contains("ROLE_MANAGER")) {
            response.sendRedirect("/serviceSpringBoot/manager/access");
        } else if (roles.contains("ROLE_MAINTENANCE")) {
            response.sendRedirect("/serviceSpringBoot/maintenance/access");
        } else if (roles.contains("ROLE_USER")) {
            response.sendRedirect("/serviceSpringBoot/consumer/access");
        } else {
            response.sendRedirect("/login?error=no-role");
        }
    }
}