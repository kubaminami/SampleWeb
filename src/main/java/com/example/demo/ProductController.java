package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductRepository proRepo;
	@Autowired
	private CategoryRepository catRepo;

	// 一覧ページ
	@GetMapping
	public String list(@RequestParam(name = "category", required = false) String category, Model model) {

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
	@GetMapping("/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		model.addAttribute("product", proRepo.findById(id).orElseThrow());
		return "product_detail";
	}
}
