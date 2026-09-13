package capp.modules.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/distributor/access")
public class DistributorAccessController {

    @GetMapping()
    public String distributor(Authentication authentication, Model model) {
        model.addAttribute("distributorId", authentication.getName());
        return "Distributor/distributor_screen";
    }

}