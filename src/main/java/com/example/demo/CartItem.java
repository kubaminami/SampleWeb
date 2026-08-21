package com.example.demo;

import lombok.Getter;

@Getter
public class CartItem {

	private final Product product;
	private int quantity;

	public CartItem(Product product) {
		this.product = product;
		this.quantity = 1;
	}

	public void increaseQuantity() {
		quantity++;
	}

	public void setQuantity(int quantity) {
		this.quantity = Math.max(1, quantity);
	}
}