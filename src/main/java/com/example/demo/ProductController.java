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
import java.util.List;
import java.util.Comparator;

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

		model.addAttribute("loginUser", loginUser);
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
	public String showEditForm(@PathVariable("id") Long id, Model model,
			@RequestParam(name = "selectedCategory", required = false) String selectedCategory,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "sort", required = false, defaultValue = "id") String sort, HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			return "redirect:/login";
		}

		Product product = proRepo.findById(id).orElseThrow();

		model.addAttribute("loginUser", loginUser);
		model.addAttribute("product", product);
		model.addAttribute("categories", catRepo.findAll());

		model.addAttribute("selectedCategory", selectedCategory);
		model.addAttribute("keyword", keyword);
		model.addAttribute("sort", sort);
		return "product-edit";
	}

	// 編集処理
	@PostMapping("/{id}/edit")
	public String updateProduct(@PathVariable("id") Long id, @ModelAttribute Product form,
			@RequestParam(name = "selectedCategory", required = false) String selectedCategory,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "sort", required = false, defaultValue = "id") String sort, Model model) {

		Product product = proRepo.findById(id).orElseThrow();

		product.setName(form.getName());
		product.setPrice(form.getPrice());
		product.setCategory(form.getCategory());
		product.setDescription(form.getDescription());
		product.setImg(form.getImg());

		proRepo.save(product);

		return "redirect:/products?selectedCategory=" + selectedCategory
				+ "&keyword=" + keyword
				+ "&sort=" + sort;
	}

	// 削除処理
	@PostMapping("/{id}/delete")
	public String deleteProduct(@PathVariable("id") Long id) {

		proRepo.deleteById(id);

		return "redirect:/products";
	}

	// 一覧ページ
	@GetMapping
	public String list(
			@RequestParam(name = "selectedCategory", required = false) String selectedCategory,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "sort", required = false, defaultValue = "id") String sort,
			Model model,
			HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);

		List<Product> products;

		if (selectedCategory != null && !selectedCategory.isBlank() && keyword != null && !keyword.isBlank()) {
			products = proRepo.findByCategoryAndNameContainingIgnoreCase(selectedCategory, keyword);
		} else if (keyword != null && !keyword.isBlank()) {
			products = proRepo.findByNameContainingIgnoreCase(keyword);
		} else if (selectedCategory != null && !selectedCategory.isBlank()) {
			products = proRepo.findByCategory(selectedCategory);
		} else {
			products = proRepo.findAllByOrderByIdAsc();
		}

		if ("priceAsc".equals(sort)) {
			products.sort(Comparator.comparing(Product::getPrice));
		} else if ("priceDesc".equals(sort)) {
			products.sort(Comparator.comparing(Product::getPrice).reversed());
		} else {
			products.sort(Comparator.comparing(Product::getId));
		}

		model.addAttribute("products", products);
		model.addAttribute("selectedCategory", selectedCategory);
		model.addAttribute("keyword", keyword);
		model.addAttribute("sort", sort);
		model.addAttribute("categories", catRepo.findAll());

		return "product_list";
	}

	// 詳細ページ
	@GetMapping("/{id:[0-9]+}")
	public String detail(@PathVariable("id") Long id,
			@RequestParam(name = "selectedCategory", required = false) String selectedCategory,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "sort", required = false, defaultValue = "id") String sort, Model model,
			HttpSession session) {
		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("product", proRepo.findById(id).orElseThrow());
		model.addAttribute("keyword", keyword);
		model.addAttribute("sort", sort);
		model.addAttribute("selectedCategory", selectedCategory);
		return "product_detail";
	}

}
