package com.example.be.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.be.common.utils.Pagination;
import com.example.be.common.utils.StringErrorValue;
import com.example.be.entity.Brand;
import com.example.be.entity.Category;
import com.example.be.entity.Product;
import com.example.be.model.StoreProcedureListResult;
import com.example.be.request.CRUDProductRequest;
import com.example.be.response.BaseListDataResponse;
import com.example.be.response.BaseResponse;
import com.example.be.response.ProductResponse;
import com.example.be.sevices.BrandService;
import com.example.be.sevices.CategoryService;
import com.example.be.sevices.ProductService;

/**
 * 
 * @author Nguyen
 *
 */
@RestController
@RequestMapping("/api/v1/product")
public class ProductController  {
    @Autowired
    public ProductService productService;

	@Autowired
	public CategoryService categoryService;

	@Autowired
	public BrandService brandService;



    @GetMapping("")
//	@PreAuthorize("hasAnyAuthority('ADMIN')")
	public ResponseEntity<BaseResponse<BaseListDataResponse<ProductResponse>>> getAll(
			@RequestParam(name = "key_search", required = false, defaultValue = "") String keySearch,
			@RequestParam(name = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(name = "page", required = false, defaultValue = "1") int page,
			@RequestParam(name = "limit", required = false, defaultValue = "10") int limit) throws Exception {
		BaseResponse<BaseListDataResponse<ProductResponse>> response = new BaseResponse<>();
		Pagination pagination = new Pagination(page, limit);
		StoreProcedureListResult<Product> listProduct = productService.spGListProduct(keySearch,
				status, pagination);

		BaseListDataResponse<ProductResponse> listData = new BaseListDataResponse<>();
		List<Product> products = listProduct.getResult();
		// Lấy danh sách brandId và categoryId duy nhất
		List<Integer> brandIds = products.stream().map(Product::getBrandId).distinct().collect(Collectors.toList());
		List<Integer> categoryIds = products.stream().map(Product::getCategoryId).distinct().collect(Collectors.toList());
		// Lấy danh sách brand và category
		List<Brand> brands = brandService.findByIds(brandIds);
		List<Category> categories = categoryService.findByIds(categoryIds);
		// Tạo map tra cứu
		Map<Integer, Brand> brandMap = brands.stream().collect(Collectors.toMap(Brand::getId, b -> b));
		Map<Integer, Category> categoryMap = categories.stream().collect(Collectors.toMap(Category::getId, c -> c));
		// Tạo list response
		List<ProductResponse> productResponses = products.stream().map(product -> {
			Brand brand = brandMap.get(product.getBrandId());
			Category category = categoryMap.get(product.getCategoryId());
			return new ProductResponse(
				product,
				brand != null ? brand.getName() : null,
				category != null ? category.getName() : null
			);
		}).collect(Collectors.toList());

		listData.setList(productResponses);
		listData.setTotalRecord(listProduct.getTotalRecord());

		response.setData(listData);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
    
    @GetMapping("/{id}")
	public ResponseEntity<BaseResponse<ProductResponse>> findOneById(@PathVariable("id") int id) throws Exception {
		BaseResponse<ProductResponse> response = new BaseResponse<>();
		Product product = productService.findOne(id);

		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Brand brand = brandService.findOne(product.getBrandId());
		Category category = categoryService.findOne(product.getCategoryId());
		
		response.setData(new ProductResponse(
			product,
			brand != null ? brand.getName() : null,
			category != null ? category.getName() : null
		));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/change-status")
	public ResponseEntity<BaseResponse<ProductResponse>> changeStatus(@PathVariable("id") int id) throws Exception {
		BaseResponse<ProductResponse> response = new BaseResponse<>();
		Product product = productService.findOne(id);

		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		product.setStatus(product.getStatus() == 1 ? 0 : 1);

		productService.update(product);
        response.setData(new ProductResponse(product));

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<BaseResponse<ProductResponse>> create(
			@Valid @RequestBody CRUDProductRequest wrapper) throws Exception {

		BaseResponse<ProductResponse> response = new BaseResponse<>();
		Product productCheck = productService.findByName(wrapper.getName());

		if (productCheck != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		Category category = categoryService.findOne(wrapper.getCategoryId());

		if (category == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.CATEGORY_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

//		Brand brand = brandService.findOne(wrapper.getBrandId());
//		if (brand == null) {
//			response.setStatus(HttpStatus.BAD_REQUEST);
//			response.setMessageError(StringErrorValue.BRAND_NOT_FOUND);
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		}

		Product product = new Product();
		product.setName(wrapper.getName());
		product.setDescription(wrapper.getDescription());
		product.setBrandId(wrapper.getBrandId());
		product.setCategoryId(wrapper.getCategoryId());
		product.setPrice(wrapper.getPrice());
		product.setAverageRating(BigDecimal.ZERO);
		product.setStatus(1);

		productService.create(product);
		response.setData(new ProductResponse(product));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/{id}/update")
	public ResponseEntity<BaseResponse<ProductResponse>> update(@PathVariable("id") int id,
			@Valid @RequestBody CRUDProductRequest wrapper) throws Exception {

		BaseResponse<ProductResponse> response = new BaseResponse<>();
		Product product = productService.findOne(id);

		if (product == null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_NOT_FOUND);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		if (!product.getName().equals(wrapper.getName())
				&& productService.findByName(wrapper.getName()) != null) {
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessageError(StringErrorValue.PRODUCT_IS_EXIST);
			return new ResponseEntity<>(response, HttpStatus.OK);

		}
		product.setName(wrapper.getName());
		product.setDescription(wrapper.getDescription());
		product.setPrice(wrapper.getPrice());
		productService.update(product);

		response.setData(new ProductResponse(product));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
