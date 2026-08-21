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

	// 商品ごとの税額を含めた小計を計算するメソッド
	public static int calculateSubtotal(CartItem item) {
		if (item == null || item.getProduct() == null) {
			return 0;
		}

		Product product = item.getProduct();
		int quantity = item.getQuantity();
		int price = product.getPrice() == null ? 0 : product.getPrice();
		int taxRate = product.getTaxRate() == null ? 0 : product.getTaxRate();
		int subtotal = price * quantity;
		int tax = (int) Math.round(subtotal * taxRate / 100.0);
		return subtotal + tax;
	}

	//カートの合計金額を計算するメソッド
	public static int calculateCartTotal(List<CartItem> cart) {
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

		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		if (cart == null) {
			cart = new ArrayList<>();
			session.setAttribute("cart", cart);
		}

		// カートに追加されていない場合は新しい商品を追加
		if (cart.stream().noneMatch(item -> item.getProduct().getId() == id)) {
			Product product = repo.findById(id).orElseThrow();
			cart.add(new CartItem(product));

			// 既にカートに追加されている場合は数量を増やす
		} else {
			for (CartItem item : cart) {
				if (item.getProduct().getId() == id) {
					item.increaseQuantity();
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

		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

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

		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
		if (cart != null) {
			for (CartItem item : cart) {
				if (item.getProduct().getId() == id) {
					item.setQuantity(quantity);
					break;
				}
			}
		}

		return ResponseEntity.noContent().build();
	}

	// カートから削除
	@PostMapping("/cart/delete/{id}")
	public String delete(@PathVariable("id") long id, HttpSession session) {

		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

		if (cart != null) {

			for (CartItem item : cart) {
				if (item.getProduct().getId() == id) {
					cart.remove(item);
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

		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
		if (cart == null || cart.isEmpty()) {
			return "redirect:/cart";
		}

		// Orders
		Orders o = new Orders();
		o.setDatetime(LocalDateTime.now());

		int total = calculateCartTotal(cart);
		o.setTotal(total);

		// 税率ごとの計算
		int subtotal = 0;
		int tax8 = 0;
		int tax10 = 0;

		for (CartItem item : cart) {
			Product p = item.getProduct();

			int productSubtotal = p.getPrice() * item.getQuantity();
			subtotal += productSubtotal;

			if (p.getTaxRate() == 8) {
				tax8 += (int) Math.round(productSubtotal * 0.08);
			} else if (p.getTaxRate() == 10) {
				tax10 += (int) Math.round(productSubtotal * 0.10);
			}
		}

		o.setUserId(loginUser.getId());
		o.setSubtotal(subtotal);
		o.setTax8(tax8);
		o.setTax10(tax10);
		
		ordersrepo.save(o);

		// OrderItems
		for (CartItem item : cart) {
			Product p = item.getProduct();
			OrderItems i = new OrderItems();
			i.setOrder(o);
			i.setName(p.getName());
			i.setPrice(p.getPrice());
			i.setQuantity(item.getQuantity());
			i.setTaxRate(p.getTaxRate());
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
