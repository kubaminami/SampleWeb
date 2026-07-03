package com.example.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("cart")
public class CartController {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private OrdersRepository ordersrepo;

	@Autowired
	private OrderItemsRepository ordersItemsRepo;

	// カート初期化
	@ModelAttribute("cart")
	public List<Product> cart() {
		return new ArrayList<>();
	}

	// カート追加
	@PostMapping("/cart/add/{id}")
	public String add(@PathVariable("id") long id, @ModelAttribute("cart") List<Product> cart) {

		Product product = repo.findById(id).orElseThrow();
		cart.add(product);

		return "redirect:/cart";
	}

	// カート表示
	@GetMapping("/cart")
	public String view() {
		return "cart";
	}

	// カートから削除
	@PostMapping("/cart/delete/{id}")
	public String delete(@PathVariable("id") long id, @ModelAttribute("cart") List<Product> cart) {

		for (Product p : cart) {
			if (p.getId() == id) {
				cart.remove(p);
				break;
			}
		}

		return "redirect:/cart";
	}

	// 注文
	@PostMapping("/order")
	public String order(@ModelAttribute("cart") List<Product> cart) {

		// Orders
		Orders o = new Orders();
		o.setDatetime(LocalDateTime.now());

		int total = 0;
		for (Product p : cart) {
			total += p.getPrice();
		}

		o.setTotal(total);
		ordersrepo.save(o);

		// OrderItems
		for (Product p : cart) {
			OrderItems i = new OrderItems();
			i.setOrder(o);
			i.setName(p.getName());
			i.setPrice(p.getPrice());
			i.setDatetime(o.getDatetime());
			ordersItemsRepo.save(i);

		}

		cart.clear();

		return "redirect:/order/complete";
	}

	// 完了ページ
	@GetMapping("/order/complete")
	public String complete() {

		return "order_complete";

	}

}
