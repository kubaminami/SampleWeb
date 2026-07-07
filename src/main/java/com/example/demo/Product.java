package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank
	@Size(max = 20)
	private String name;

	@NotNull
	@Min(0)
	private Integer price;
	
	@NotBlank
	@Size(max = 255)
	private String description;
	
	@NotBlank
	@Size(max = 20)
	private String category;
	
	@NotBlank
	@Size(max = 255)
	private String img;
}
