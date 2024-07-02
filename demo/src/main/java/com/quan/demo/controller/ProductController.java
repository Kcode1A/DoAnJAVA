//package com.quan.demo.controller;
//
//import com.quan.demo.model.Product;
//import com.quan.demo.service.CategoryService;
//import com.quan.demo.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import jakarta.validation.Valid;
//@Controller
//@RequestMapping("/products")
//public class ProductController {
//    @Autowired
//    private ProductService productService;
//    @Autowired
//    private CategoryService categoryService; // Đảm bảo bạn đã inject CategoryService
//    // Display a list of all products
//    @GetMapping
//    public String showProductList(Model model) {
//        model.addAttribute("products", productService.getAllProducts());
//        return "/products/product-list";
//    }
//    // For adding a new product
//    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        model.addAttribute("product", new Product());
//        model.addAttribute("categories", categoryService.getAllCategories()); //Load categories
//        return "/products/add-product";
//    }
//    // Process the form for adding a new product
//    @PostMapping("/add")
//    public String addProduct(@Valid Product product, BindingResult result) {
//        if (result.hasErrors()) {
//            return "/products/add-product";
//        }
//        productService.addProduct(product);
//        return "redirect:/products";
//    }
//    // For editing a product
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Long id, Model model) {
//        Product product = productService.getProductById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
//        model.addAttribute("product", product);
//        model.addAttribute("categories", categoryService.getAllCategories()); //Load categories
//        return "/products/update-product";
//    }
//    // Process the form for updating a product
//    @PostMapping("/update/{id}")
//    public String updateProduct(@PathVariable Long id, @Valid Product product,
//                                BindingResult result) {
//        if (result.hasErrors()) {
//            product.setId(id); // set id to keep it in the form in case of errors
//            return "/products/update-product";
//        }
//        productService.updateProduct(product);
//        return "redirect:/products";
//
//    }
//    // Handle request to delete a product
//    @GetMapping("/delete/{id}")
//    public String deleteProduct(@PathVariable Long id) {
//        productService.deleteProductById(id);
//        return "redirect:/products";
//    }
//}

package com.quan.demo.controller;

import com.quan.demo.model.Product;
import com.quan.demo.service.CategoryService;
import com.quan.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping
    public String showProductList(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "/products/product-list";
    }
    // For adding a new product
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories()); //Load categories
        return "/products/add-product";
    }
    // Process the form for adding a new product
    @PostMapping("/add")
    public String addProduct(@Valid Product product, BindingResult result,
                             @RequestParam("file") MultipartFile file) throws IOException {
        if (result.hasErrors()) {
            return "/products/add-product";
        }
        String imageUrl = saveImage(file); // Lưu hình ảnh và nhận URL
        product.setImageUrl(imageUrl); // Thiết lập URL cho sản phẩm
        productService.addProduct(product,file);
        return "redirect:/products";
    }
    // For editing a product
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories()); //Load categories
        return "/products/update-product";
    }
    // Process the form for updating a product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid Product product,
                                BindingResult result) {
        if (result.hasErrors()) {
            product.setId(id); // set id to keep it in the form in case of errors
            return "/products/update-product";
        }
        productService.updateProduct(product);
        return "redirect:/products";
    }
    // Handle request to delete a product
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
    @GetMapping("/detail/{id}")
    public String showProductDetail(@PathVariable("id") Long id, Model model) {
        // Sử dụng ProductService để lấy thông tin sản phẩm từ ID
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + id));

        // Thêm sản phẩm vào model để truyền tới view template
        model.addAttribute("product", product);

        // Trả về tên của view template cho trang chi tiết sản phẩm
        return "/products/detail-product";
    }
    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {
        List<Product> searchResults = productService.search(keyword); // ProductService cần có phương thức search(keyword)
        model.addAttribute("products", searchResults);
        model.addAttribute("keyword", keyword);
        return "/products/product-list"; // Trả về view template hiển thị kết quả tìm kiếm
    }
    // Method to save image and return its URL
    private String saveImage(MultipartFile imageFile) throws IOException {
        // Implement your logic to save image file and return its URL
        return "/images/" + imageFile.getOriginalFilename(); // Example URL
    }
}

