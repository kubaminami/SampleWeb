package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String showSignupForm(Model model) {

        model.addAttribute(
                "signupForm",
                new SignupForm());

        return "signup";
    }

    @PostMapping("/signup")
    public String signup(
            @ModelAttribute SignupForm signupForm) {

        userService.register(signupForm);

        return "redirect:/login";
    }
}