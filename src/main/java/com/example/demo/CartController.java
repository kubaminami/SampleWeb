package com.example.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

	@Autowired
	private ProductRepository repo;

	@Autowired
	private OrdersRepository ordersrepo;

	@Autowired
	private OrderItemsRepository ordersItemsRepo;

	// カート初期化
	public List<Product> cart() {
		return new ArrayList<>();
	}

	// カート追加
	@PostMapping("/cart/add/{id}")
	public String add(@PathVariable("id") long id, HttpSession session) {

		List<Product> cart = (List<Product>) session.getAttribute("cart");

		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute("cart", cart);
		}

		Product product = repo.findById(id).orElseThrow();
		cart.add(product);

		return "redirect:/cart";
	}

	// カート表示
	@GetMapping("/cart")
	public String view(HttpSession session, Model model) {

		List<Product> cart = (List<Product>) session.getAttribute("cart");

		if (cart == null) {
			cart = new ArrayList<>();
		}

		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("cart", cart);

		return "cart";
	}

	// カートから削除
	@PostMapping("/cart/delete/{id}")
	public String delete(@PathVariable("id") long id, HttpSession session) {

		List<Product> cart = (List<Product>) session.getAttribute("cart");

		if (cart != null) {

			for (Product p : cart) {
				if (p.getId() == id) {
					cart.remove(p);
					break;
				}
			}
		}

		return "redirect:/cart";
	}

	// 注文
	@PostMapping("/order")
	public String order(HttpSession session, RedirectAttributes redirectAttributes) {

		// ログインしていない場合
		User loginUser = (User) session.getAttribute("loginUser");

		if (loginUser == null) {
			redirectAttributes.addFlashAttribute("message", "注文するにはログインしてください。");
			return "redirect:/login";
		}

		List<Product> cart = (List<Product>) session.getAttribute("cart");
		if (cart == null || cart.isEmpty()) {
			return "redirect:/cart";
		}

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
	public String complete(Model model, HttpSession session) {

		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);

		return "order_complete";

	}

}
