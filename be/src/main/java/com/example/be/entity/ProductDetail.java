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
@Table(name = "product_details")
public class ProductDetail extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;

	@Column(name = "product_id")
	private int productId;

	@Column(name = "color_id")
	private int colorId;

	private String color;

	@Column(name = "size_id")
	private int sizeId;

	private String size;

	@Column(name = "material_id")
	private int materialId;

	private String material;

	private int stock;

	private BigDecimal price;

	@Column(name = "image_url")
	private String imageUrl; 

	private int status;

	@Column(name = "brand_id")
	private int brandId;

	private String brand;

	@Column(name = "category_id")
	private int categoryId;

	private String category;
	
	private String sku;

	@Column(name = "barcode", unique = true, length = 13)
	private String barcode;
}
