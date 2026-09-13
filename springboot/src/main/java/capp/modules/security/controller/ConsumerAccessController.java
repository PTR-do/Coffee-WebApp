package capp.modules.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import capp.modules.security.service.ConsumerAccessService;

@Controller
@RequestMapping("/consumer/access")
public class ConsumerAccessController {
    private final ConsumerAccessService accessService;
    public ConsumerAccessController(ConsumerAccessService accessService) {
        this.accessService = accessService;
    }

    @GetMapping()
    public String consumer(Authentication authentication, Model model) {
        String username = authentication.getName();
        String credit = accessService.getConsumerCredit(username);
        model.addAttribute("username", username);
        model.addAttribute("credit", credit);
        return "Consumer/consumer_home";
    }

    @GetMapping("/map")
    public String accessMap() {
        return "Consumer/consumer_map";
    }

    @GetMapping("/home")
    public String returnHome() {
        return "redirect:/consumer/access";
    }

}