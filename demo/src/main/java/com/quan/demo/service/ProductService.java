package com.quan.demo.service;

import com.quan.demo.model.Product;
import com.quan.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private static final String UPLOAD_DIR = "uploads/images/";
    private final ProductRepository productRepository;
    // Retrieve all products from the database
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    // Retrieve a product by its id
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    // Add a new product to the database
    public void addProduct(Product product, MultipartFile file) throws IOException {
        saveProductImage(product, file);
        productRepository.save(product);
    }
    // Update an existing product
    public Product updateProduct(@NotNull Product product) {
        Product existingProduct = productRepository.findById(product.getId())
                .orElseThrow(() -> new IllegalStateException("Product with ID " +
                        product.getId() + " does not exist."));
        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        return productRepository.save(existingProduct);
    }
    // Delete a product by its id
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalStateException("Product with ID " + id + " does not exist.");
        }
        productRepository.deleteById(id);
    }
    private void saveProductImage(Product product, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Check if file already exists
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            if (Files.exists(filePath)) {
                // File already exists, reuse the existing file's URL
                product.setImageUrl("/images/" + fileName);
            } else {
                // Save the new file
                Files.copy(file.getInputStream(), filePath);
                product.setImageUrl("/images/" + fileName);
            }
        }
    }
    public List<Product> search(String keyword) {
        return productRepository.findByKeyword(keyword);
    }
}