package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public void register(SignupForm form) {

		User existingUser = userRepository.findByEmail(form.getEmail());

		if (existingUser != null) {
			throw new RuntimeException("メールアドレスは既に登録されています");
		}

		User user = new User();

		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPassword(form.getPassword());

		userRepository.save(user);
	}
}
