package com.quan.demo.controller;

import com.quan.demo.service.AdminService;
import com.quan.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    @Autowired
    private AdminService adminService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Thêm dữ liệu vào model nếu cần
        return "admin/dashboard";
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        // Thêm dữ liệu vào model nếu cần
        return "admin/categories";
    }

    @GetMapping("/products")
    public String products(Model model) {
        // Thêm dữ liệu vào model nếu cần
        return "admin/products";
    }

    @GetMapping("/user-list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/userList"; // Template này bạn phải có
    }

    @GetMapping("/statistics")
    public String showStatistics(Model model) {
        model.addAttribute("userCount", adminService.countUsers());
        model.addAttribute("movieCount", adminService.countMovies());
        return "admin/statistics";
    }
    @PostMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "admin/userList";
    }



    // Các phương thức khác cho admin
}
