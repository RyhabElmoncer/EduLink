package com.EduLink.RouteController;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RouteController {
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "success", required = false) String successMessage,
            Model model) {
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }
        return "login";
    }
    @GetMapping("/inscription")
    public String inscriptionPage(Model model) {
        return "inscription";
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";     }
    @GetMapping("/")
    public String defaultPage() {
        return "redirect:/login"; // Redirige automatiquement vers `/login`
    }


}
