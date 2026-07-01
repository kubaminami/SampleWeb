package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/orders")
@Controller
public class OrderController {

	@Autowired
	private OrdersRepository ordersRepo;

	// 注文履歴
	@GetMapping
	public String list(Model model) {
		model.addAttribute("orders", ordersRepo.findAll());
		return "order_list";
	}

	// 注文詳細
	@GetMapping("/{no}")
	public String detail(@PathVariable("no") Long no, Model model) {
		model.addAttribute("order", ordersRepo.findById(no).orElseThrow());

		return "order_detail";
	}
}
