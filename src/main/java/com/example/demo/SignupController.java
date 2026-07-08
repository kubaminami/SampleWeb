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
	private UserRepository userRepository;

	@GetMapping("/signup")
	public String showSignupForm(Model model) {

		model.addAttribute("signupForm", new SignupForm());

		return "signup";
	}


	@PostMapping("/signup")
	public String signup(@ModelAttribute SignupForm signupForm, Model model) {

		if (signupForm.getName() == null || signupForm.getName().isBlank() ||
				signupForm.getEmail() == null || signupForm.getEmail().isBlank() || signupForm.getPassword() == null
				|| signupForm.getPassword().isBlank()) {

			model.addAttribute("message", "全て入力してください。");

			return "signup";
		}
		
		User existingUser = userRepository.findByEmail(signupForm.getEmail());

		if (existingUser != null) {
			model.addAttribute("message", "メールアドレスは既に登録されています");
			
			return "signup";
		}

		User user = new User();

		user.setName(signupForm.getName());
		user.setEmail(signupForm.getEmail());
		user.setPassword(signupForm.getPassword());

		userRepository.save(user);

		return "redirect:/login";
	}
}