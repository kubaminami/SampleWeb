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

	@Autowired
	private AdminUserRepository adminUserRepository;

	// ユーザー用ログインページ
	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}

	// ユーザー用ログイン処理
	@PostMapping("/login")
	public String searchLoginUser(LoginForm form, HttpSession session, Model model) {

		// 未入力がある
		if (form.getEmail() == null || form.getEmail().isBlank() || form.getPassword() == null
				|| form.getPassword().isBlank()) {

			model.addAttribute("message", "メールアドレスとパスワードを入力してください。");

			return "login";
		}

		// メールアドレスが登録されていない
		User user = userRepository.findByEmail(form.getEmail());

		if (user == null) {

			model.addAttribute("message", "メールアドレスまたはパスワードが違います。");

			return "login";
		}

		// メールアドレスとパスワードの組み合わせが一致しない
		if (!user.getPassword().equals(form.getPassword())) {
			model.addAttribute("message", "メールアドレスまたはパスワードが違います。");

			return "login";
		}

		// ログインする
		session.setAttribute("loginUser", user);
		return "redirect:/products";
	}

	// ユーザー用ログアウト処理
	@GetMapping("/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/login";
	}

	// ユーザー用マイページを表示
	@GetMapping("/mypage")
	public String mypage(HttpSession session, Model model) {

		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("loginUser", loginUser);

		return "mypage";
	}

	// 管理者用ログインページ
	@GetMapping("/admin/login")
	public String showAdminLoginForm() {
		return "admin_login";
	}

	// 管理者用ログイン処理
	@PostMapping("/admin/login")
	public String adminLogin(AdminLoginForm form, HttpSession session, Model model) {

		// 未入力がある
		if (form.getEmail() == null || form.getEmail().isBlank() || form.getPassword() == null
				|| form.getPassword().isBlank()) {

			model.addAttribute("message", "メールアドレスとパスワードを入力してください。");

			return "admin_login";
		}

		// メールアドレスが登録されていない
		AdminUser adminLoginUser = adminUserRepository.findByEmail(form.getEmail());

		if (adminLoginUser == null) {

			model.addAttribute("message", "メールアドレスまたはパスワードが違います。");

			return "admin_login";
		}

		// メールアドレスとパスワードの組み合わせが一致しない
		if (!adminLoginUser.getPassword().equals(form.getPassword())) {
			model.addAttribute("message", "メールアドレスまたはパスワードが違います。");

			return "admin_login";
		}

		// ログインする
		session.setAttribute("adminLoginUser", adminLoginUser);
		return "redirect:/products/admin";
	}
}