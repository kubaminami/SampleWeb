package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Sort {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
    
    private Integer no;

    private String code;

    private String name;

    private Integer display;

}
