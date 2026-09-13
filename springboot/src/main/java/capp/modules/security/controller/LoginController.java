package capp.modules.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String defaultLogin() {
        return "redirect:/login/consumer";
    }
    @GetMapping("/login/consumer")
    public String consumerLogin() {
        return "login/consumer_login";
    }

    @GetMapping("/login/distributor")
    public String distributorLogin() {
        return "login/distributor_start";
    }

    @GetMapping("/login/maintenance")
    public String maintenanceLogin() {
        return "login/maintenance_login";
    }

    @GetMapping("/login/manager")
    public String managerLogin() {
        return "login/manager_login";
    }

}