package com.example.be.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "products")
public class Product extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	@Column(name = "brand_id")
	private int brandId;

	@Column(name = "category_id")
	private int categoryId;
	

	private String description;

	private BigDecimal price;

	@Column(name = "average_rating")
	private BigDecimal averageRating;

	@Column(name = "image_url")
	private String imageUrl; 

	private int status;
}
