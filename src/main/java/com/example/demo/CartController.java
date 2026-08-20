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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
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

	public static int calculateSubtotal(Product product) {
		if (product == null) {
			return 0;
		}

		int quantity = product.getQuantity() == null ? 0 : product.getQuantity();
		int price = product.getPrice() == null ? 0 : product.getPrice();
		int taxRate = product.getTaxRate() == null ? 0 : product.getTaxRate();
		int subtotal = price * quantity;
		int tax = (int) Math.round(subtotal * taxRate / 100.0);
		return subtotal + tax;
	}

	public static int calculateCartTotal(List<Product> cart) {
		if (cart == null || cart.isEmpty()) {
			return 0;
		}

		return cart.stream().mapToInt(CartController::calculateSubtotal).sum();
	}

	// カート追加
	@PostMapping("/cart/add/{id}")
	public String add(@PathVariable("id") long id, HttpSession session,
			@RequestParam(name = "selectedCategory", required = false) String selectedCategory,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "sort", required = false, defaultValue = "id") String sort, RedirectAttributes attrs) {

		List<Product> cart = (List<Product>) session.getAttribute("cart");

		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute("cart", cart);
		}

		// カートに追加されていない場合は新しい商品を追加
		if (cart.stream().noneMatch(p -> p.getId() == id)) {
			Product product = repo.findById(id).orElseThrow();
			product.setQuantity(1); // 初期数量を1に設定
			cart.add(product);

			// 既にカートに追加されている場合は数量を増やす
		} else {
			for (Product p : cart) {
				if (p.getId() == id) {
					p.setQuantity(p.getQuantity() + 1);
					break;
				}
			}
		}

		if (selectedCategory != null) {
			attrs.addAttribute("selectedCategory", selectedCategory);
		}

		if (keyword != null) {
			attrs.addAttribute("keyword", keyword);
		}

		attrs.addAttribute("sort", sort);

		return "redirect:/cart";

	}

	// カート表示
	@GetMapping("/cart")
	public String view(HttpSession session, Model model,
			@RequestParam(name = "selectedCategory", required = false) String selectedCategory,
			@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "sort", required = false, defaultValue = "id") String sort) {

		List<Product> cart = (List<Product>) session.getAttribute("cart");

		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute("cart", cart);
		}

		User loginUser = (User) session.getAttribute("loginUser");
		model.addAttribute("loginUser", loginUser);
		model.addAttribute("cart", cart);
		model.addAttribute("total", calculateCartTotal(cart));

		model.addAttribute("selectedCategory", selectedCategory);
		model.addAttribute("keyword", keyword);
		model.addAttribute("sort", sort);

		return "cart";
	}

	// カート数量更新
	@PostMapping("/cart/update/{id}")
	public ResponseEntity<Void> updateQuantity(@PathVariable("id") long id,
			@RequestParam(name = "quantity", defaultValue = "1") int quantity, HttpSession session) {

		List<Product> cart = (List<Product>) session.getAttribute("cart");
		if (cart != null) {
			for (Product product : cart) {
				if (product.getId() == id) {
					product.setQuantity(Math.max(1, quantity));
					break;
				}
			}
		}

		return ResponseEntity.noContent().build();
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

		int total = calculateCartTotal(cart);
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
