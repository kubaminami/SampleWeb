package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}

	@PostMapping("/login")
	public String searchLoginUser(LoginForm form, HttpSession session) {

		User user = userRepository.findByEmail(form.getEmail());

		if (user == null) {
			return "login";
		}

		if (user.getPassword().equals(form.getPassword())) {

			session.setAttribute("loginUser", user);

			return "redirect:/products";
		}

		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/login";
	}

	@GetMapping("/mypage")
	public String mypage(HttpSession session, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("loginUser", loginUser);

		return "mypage";
	}
}