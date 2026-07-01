package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

	private final CategoryRepository categoryRepository;

	public GlobalControllerAdvice(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	//全てのHTMLでcategoriesを使用できるようにする
	@ModelAttribute("categories")
	public List<Category> addCategoryies(){
		return categoryRepository.findAll();
	}
}
