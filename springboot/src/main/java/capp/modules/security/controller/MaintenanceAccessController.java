package capp.modules.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/maintenance/access")
public class MaintenanceAccessController {

    @GetMapping()
    public String maintenance(Authentication authentication, Model model) {
        model.addAttribute("maintainerId", authentication.getName());
        return "Maintenance/maintenance_home";
    }
}