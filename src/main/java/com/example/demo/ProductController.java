package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductRepository proRepo;
	@Autowired
	private CategoryRepository catRepo;

	// 商品登録ページ
	@GetMapping("/new")
	public String showCreateForm(Model model, HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			return "redirect:/login";
		}

		model.addAttribute("product", new Product());
		model.addAttribute("categories", catRepo.findAll());
		return "product-form";
	}

	// 登録処理
	@PostMapping
	public String createProduct(@Valid @ModelAttribute Product product, BindingResult result, Model model) {

		if (result.hasErrors()) {
			model.addAttribute("categories", catRepo.findAll());
			return "product-form";
		}

		proRepo.save(product);
		return "redirect:/products";
	}

	// 商品編集ページ
	@GetMapping("/{id}/edit")
	public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			return "redirect:/login";
		}

		Product product = proRepo.findById(id).orElseThrow();

		model.addAttribute("product", product);
		model.addAttribute("categories", catRepo.findAll());

		return "product-edit";
	}

	// 編集処理
	@PostMapping("/{id}/edit")
	public String updateProduct(@PathVariable("id") Long id, @ModelAttribute Product form) {

		Product product = proRepo.findById(id).orElseThrow();

		product.setName(form.getName());
		product.setPrice(form.getPrice());
		product.setCategory(form.getCategory());
		product.setDescription(form.getDescription());
		product.setImg(form.getImg());

		proRepo.save(product);

		return "redirect:/products";
	}

	// 削除処理
	@PostMapping("/{id}/delete")
	public String deleteProduct(@PathVariable("id") Long id) {

		proRepo.deleteById(id);

		return "redirect:/products";
	}

	// 一覧ページ
	@GetMapping
	public String list(@RequestParam(name = "category", required = false) String category, Model model,
			HttpSession session) {

		//ログイン情報がある場合
		if (session != null) {
			User loginUser = (User) session.getAttribute("loginUser");
			model.addAttribute("loginUser", loginUser);
		}

		if (category == null) {
			model.addAttribute("products", proRepo.findAll());
		} else {
			model.addAttribute("products", proRepo.findByCategory(category));
		}

		model.addAttribute("selectedCategory", category);
		model.addAttribute("categories", catRepo.findAll());

		return "product_list";
	}

	// 詳細ページ
	@GetMapping("/{id:[0-9]+}")
	public String detail(@PathVariable("id") Long id, Model model) {
		model.addAttribute("product", proRepo.findById(id).orElseThrow());
		return "product_detail";
	}

}
