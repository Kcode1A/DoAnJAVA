package com.quan.demo.controller;

import com.quan.demo.model.Movie;
import com.quan.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private final MovieService movieService;

    @Autowired
    public HomeController(MovieService movieService) {
        this.movieService = movieService;
    }

//    @GetMapping
//    public String home(Model model) {
//        List<Movie> movies = movieService.getAllMoviesbyday();
//        model.addAttribute("movies", movies);
//        return "home/index"; // Tên của file Thymeleaf template (home/index.html)
//    }

    @GetMapping("/home")
    public String home(Model model, @RequestParam(defaultValue = "0") int page) {
        // Number of items per page
        int pageSize = 3; // Adjust this as per your requirement

        // Retrieve a page of movies from the service
        Page<Movie> moviePage = movieService.findPaginated(PageRequest.of(page, pageSize));

        // Add movies and pagination information to the model
        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "home/index"; // Tên của file Thymeleaf template (home/index.html)
    }
    @GetMapping("home/search")
    public String searchByKeyword(Model model, @RequestParam String keyword, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 3; // Số lượng phim trên mỗi trang
        // Tìm kiếm phim với từ khoá
        Page<Movie> moviePage = movieService.findByKeyword(keyword, PageRequest.of(page, pageSize));

        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        model.addAttribute("keyword", keyword); // Thêm keyword vào model để sử dụng trong phân trang
        return "home/index";
    }
}
