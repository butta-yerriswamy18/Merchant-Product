package org.jsp.merchantproductapp.service;

import java.util.List;
import java.util.Optional;
import org.jsp.merchantproductapp.dao.MerchantDao;
import org.jsp.merchantproductapp.dao.ProductDao;
import org.jsp.merchantproductapp.dto.Merchant;
import org.jsp.merchantproductapp.dto.Product;
import org.jsp.merchantproductapp.dto.ResponseStructure;
import org.jsp.merchantproductapp.exception.MerchantNotFoundException;
import org.jsp.merchantproductapp.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private MerchantDao merchantDao;

	public ResponseEntity<ResponseStructure<Product>> saveProduct(Product product, int merchant_id) {
		ResponseStructure<Product> structure = new ResponseStructure<>();
		Optional<Merchant> dbMerchant = merchantDao.findById(merchant_id);
		if (dbMerchant.isPresent()) {
			Merchant merchant = dbMerchant.get();
			merchant.getProducts().add(product);
			product.setMerchant(merchant);
			merchantDao.saveMerchant(merchant);
			structure.setData(productDao.saveProduct(product));
			structure.setMessage("Product added");
			structure.setStatusCode(HttpStatus.CREATED.value());
			return new ResponseEntity<ResponseStructure<Product>>(structure, HttpStatus.CREATED);
		}
		throw new MerchantNotFoundException("Invalid Merchant Id");
	}

	public ResponseEntity<ResponseStructure<Product>> updateProduct(Product product) {
		ResponseStructure<Product> structure = new ResponseStructure<>();
		Optional<Product> recProduct = productDao.findById(product.getId());
		if (recProduct.isPresent()) {
			Product dbProduct = recProduct.get();
			dbProduct.setBrand(product.getBrand());
			dbProduct.setCategory(product.getCategory());
			dbProduct.setDescription(product.getDescription());
			dbProduct.setCost(product.getCost());
			dbProduct.setImage_url(product.getImage_url());
			dbProduct.setName(product.getName());
			structure.setData(productDao.saveProduct(dbProduct));
			structure.setMessage("Product Updated");
			structure.setStatusCode(HttpStatus.ACCEPTED.value());
			return new ResponseEntity<ResponseStructure<Product>>(structure, HttpStatus.ACCEPTED);
		}
		throw new ProductNotFoundException("Invalid Id");
	}

	public ResponseEntity<ResponseStructure<Product>> findById(int id) {
		ResponseStructure<Product> structure = new ResponseStructure<>();
		Optional<Product> dbProduct = productDao.findById(id);
		if (dbProduct.isPresent()) {
			structure.setData(dbProduct.get());
			structure.setMessage("Product Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<Product>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("Invalid Product Id");
	}

	public ResponseEntity<ResponseStructure<List<Product>>> findByBrand(String brand) {
		ResponseStructure<List<Product>> structure = new ResponseStructure<>();
		List<Product> products = productDao.findByBrand(brand);
		if (products.size() > 0) {
			structure.setData(products);
			structure.setMessage("Products Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("Invalid Brand");
	}

	public ResponseEntity<ResponseStructure<List<Product>>> findByCategory(String category) {
		ResponseStructure<List<Product>> structure = new ResponseStructure<>();
		List<Product> products = productDao.findByCategory(category);
		if (products.size() > 0) {
			structure.setData(products);
			structure.setMessage("Products Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("Invalid Category");
	}

	public ResponseEntity<ResponseStructure<List<Product>>> findByMerchantId(int id) {
		ResponseStructure<List<Product>> structure = new ResponseStructure<>();
		List<Product> products = productDao.findByMerchantId(id);
		if (products.size() > 0) {
			structure.setData(products);
			structure.setMessage("Products Found");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("Invalid Merchant Id");
	}

	public ResponseEntity<ResponseStructure<Product>> delete(int id) {
		ResponseStructure<Product> structure = new ResponseStructure<>();
		Optional<Product> dbProduct = productDao.findById(id);
		if (dbProduct.isPresent()) {
			productDao.delete(id);
			structure.setData(dbProduct.get());
			structure.setMessage("Product Deleted");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<Product>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("Product Not Found");
	}

	public ResponseEntity<ResponseStructure<List<Product>>> findAll() {
		ResponseStructure<List<Product>> structure = new ResponseStructure<>();
		List<Product> products = productDao.findAll();
		if (products.size() > 0) {
			structure.setData(products);
			structure.setMessage("List of Products");
			structure.setStatusCode(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<List<Product>>>(structure, HttpStatus.OK);
		}
		throw new ProductNotFoundException("No Products Present");
	}
}
