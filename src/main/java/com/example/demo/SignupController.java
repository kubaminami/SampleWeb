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

	//登録ページを表示
	@GetMapping("/signup")
	public String showSignupForm(Model model) {

		model.addAttribute("signupForm", new SignupForm());

		return "signup";
	}

	//登録処理
	@PostMapping("/signup")
	public String signup(@ModelAttribute SignupForm signupForm, Model model) {

		//未入力がある
		if (signupForm.getName() == null || signupForm.getName().isBlank() ||
				signupForm.getEmail() == null || signupForm.getEmail().isBlank() || signupForm.getPassword() == null
				|| signupForm.getPassword().isBlank()) {

			model.addAttribute("message", "全て入力してください。");

			return "signup";
		}

		//桁数チェック 名前は50文字、メールアドレスは255文字以内、パスワードは64文字以内
		if (signupForm.getName().length() >= 50 ){
			model.addAttribute("message", "名前は50文字以内で入力してください。");
			return "signup";
		}

		if (signupForm.getEmail().length() >= 255) {
			model.addAttribute("message", "メールアドレスは255文字以内で入力してください。");
			return "signup";
		}

		if (signupForm.getPassword().length() >= 64) {
			model.addAttribute("message", "パスワードは64文字以内で入力してください。");
			return "signup";
		}

		//パスワード半角英数字チェック
		if (!signupForm.getPassword().matches("^[a-zA-Z0-9]+$")) {
			model.addAttribute("message", "パスワードは半角英数字で入力してください。");
			return "signup";
		}

		//メールアドレスの重複
		User existingUser = userRepository.findByEmail(signupForm.getEmail());

		if (existingUser != null) {
			model.addAttribute("message", "メールアドレスは既に登録されています");
			
			return "signup";
		}

		//登録する
		User user = new User();

		user.setName(signupForm.getName());
		user.setEmail(signupForm.getEmail());
		user.setPassword(signupForm.getPassword());

		userRepository.save(user);

		return "redirect:/login";
	}
}