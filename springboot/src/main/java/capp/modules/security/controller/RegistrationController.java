package capp.modules.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import capp.modules.security.dto.RegistrationRequestDTO;
import capp.modules.security.service.RegistrationService;

@Controller
@RequestMapping("/registration")
public class RegistrationController {
    private final RegistrationService registrationService;
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping
    public String showRegistrationForm() {
        return "login/consumer_registration";
    }

    @PostMapping
    public String handleRegistration(@ModelAttribute RegistrationRequestDTO request, RedirectAttributes redirectAttributes) {
        String result = registrationService.registerUser(request);
        if (result.equals("success")) {
            return "redirect:consumer/access/";
        } else {
            redirectAttributes.addFlashAttribute("error", result);
            return "redirect:/registration";
        }
    }
}