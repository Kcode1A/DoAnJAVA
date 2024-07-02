//package com.quan.demo.controller;
//
//import com.quan.demo.model.Comment;
//import com.quan.demo.model.Movie;
//import com.quan.demo.service.CategoryService;
//import com.quan.demo.service.CommentService;
//import com.quan.demo.service.MovieService;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Controller
//@RequestMapping("/movies")
//@Slf4j
//public class MovieController {
//    private final MovieService movieService;
//    private final CommentService commentService;
//    private final CategoryService categoryService;
//
//    @Autowired
//    public MovieController(MovieService movieService, CommentService commentService, CategoryService categoryService) {
//        this.movieService = movieService;
//        this.commentService = commentService;
//        this.categoryService = categoryService;
//    }
//
//    @GetMapping
//    public String getAllMovies(Model model) {
//        model.addAttribute("movies", movieService.getAllMovies());
//        return "movie/index";
//    }
//
//    @GetMapping("/new")
//    public String createMovieForm(Model model) {
//        model.addAttribute("movie", new Movie());
//        model.addAttribute("categories", categoryService.getAllCategories());
//        return "movie/movie_form";
//    }
//
//    public String saveMovie(@ModelAttribute Movie movie,
//                            @RequestParam("file") MultipartFile file)throws IOException{
//        String imageUrl = saveImage(file); // Lưu hình ảnh và nhận URL
//        movie.setImageUrl(imageUrl); // Thiết lập URL cho sản phẩm
//        movieService.addMovie(movie,file);
//        movieService.saveMovie(movie);
//        return "redirect:/movies";
//    }
//
//    @GetMapping("/detail/{id}")
//    public String showMovieDetail(@PathVariable("id") Long id, Model model) {
//        Movie movie = movieService.getMovieById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid movie ID: " + id));
//        List<Comment> comments = commentService.findAllByMovieId(id);
//        model.addAttribute("movie", movie);
//        model.addAttribute("comments", comments);
//        model.addAttribute("newComment", new Comment());
//        return "movie/movie_detail";
//    }
//
//    @PostMapping("/addComment/{movieId}")
//    public String addComment(@PathVariable Long movieId, @ModelAttribute Comment newComment, Authentication authentication) {
//        newComment.setCreatedAt(LocalDateTime.now());
//        String username = authentication.getName();
//        newComment.setUsername(username);
//        Movie movie = movieService.getMovieById(movieId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid movie ID: " + movieId));
//        newComment.setMovie(movie);
//        commentService.saveComment(newComment);
//        return "redirect:/movies/detail/{movieId}";
//    }
//
//    @PostMapping("/deleteComment/{commentId}")
//    public String deleteComment(@PathVariable Long commentId) {
//        Long movieId = commentService.getMovieIdByCommentId(commentId);
//        commentService.deleteComment(commentId);
//        return "redirect:/movies/detail/" + movieId;
//    }
//
//    @GetMapping("/edit/{id}")
//    public String editMovieForm(@PathVariable Long id, Model model) {
//        Movie movie = movieService.getMovieById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid movie ID: " + id));
//        model.addAttribute("movie", movie);
//        model.addAttribute("categories", categoryService.getAllCategories());
//        return "movie/update_movie";
//    }
//
//    //    @PostMapping("/update/{id}")
////    public String updateMovie(@PathVariable Long id, @Valid @ModelAttribute("movie") Movie movie,
////                              BindingResult result) {
////        if (result.hasErrors()) {
////
////            movie.setId(id); // set id to keep it in the form in case of errors
////            return "movie/update_movie";
////        }
////
////        movieService.saveMovie(movie);
////        return "redirect:/movies/detail/" + id;
////    }
//    @PostMapping("/update/{id}")
//    public String updateMovie(@PathVariable Long id, @Valid @ModelAttribute("movie") Movie movie,
//                              BindingResult result, @RequestParam("file") MultipartFile file) throws IOException {
//        if (result.hasErrors()) {
//            movie.setId(id); // set id to keep it in the form in case of errors
//            return "movie/update_movie";
//        }
//
//        // Save the movie (without image update)
//        movieService.saveMovie(movie);
//
//        // Update the movie image
//        movieService.updateMovieImage(id, file);
//
//        return "redirect:/movies/detail/" + id;
//    }
//
//    @GetMapping("/delete/{id}")
//    public String deleteMovie(@PathVariable Long id) {
//        if (movieService.getMovieById(id).isPresent()) {
//            movieService.deleteMovie(id);
//        } else {
//            throw new IllegalArgumentException("Invalid movie ID: " + id);
//        }
//        return "redirect:/movies";
//    }
//
//    private String saveImage(MultipartFile imageFile) throws IOException {
//        // Implement your logic to save image file and return its URL
//        return "/images/" + imageFile.getOriginalFilename(); // Example URL
//    }
//}


package com.quan.demo.controller;

import com.quan.demo.model.Comment;
import com.quan.demo.model.Movie;
import com.quan.demo.service.CategoryService;
import com.quan.demo.service.CommentService;
import com.quan.demo.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/movies")
@Slf4j
public class MovieController {
    private final MovieService movieService;
    private final CommentService commentService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    public MovieController(MovieService movieService, CommentService commentService) {
        this.movieService = movieService;
        this.commentService = commentService;
    }

    @GetMapping
    public String home(Model model, @RequestParam(defaultValue = "0") int page) {
        // Number of items per page
        int pageSize = 3; // Adjust this as per your requirement

        // Retrieve a page of movies from the service
        Page<Movie> moviePage = movieService.findPaginated(PageRequest.of(page, pageSize));

        // Add movies and pagination information to the model
        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        return "movie/index"; // Tên của file Thymeleaf template (home/index.html)
    }
    @GetMapping("/search")
    public String searchByKeyword(Model model, @RequestParam String keyword, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 3; // Số lượng phim trên mỗi trang
        // Tìm kiếm phim với từ khoá
        Page<Movie> moviePage = movieService.findByKeyword(keyword, PageRequest.of(page, pageSize));

        model.addAttribute("movies", moviePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", moviePage.getTotalPages());
        model.addAttribute("keyword", keyword); // Thêm keyword vào model để sử dụng trong phân trang
        return "movie/index";
    }
    //    @GetMapping("/search")
//    public String searchMovies(Model model, @RequestParam(defaultValue = "") String keyword,
//                               @RequestParam(defaultValue = "0") int page) {
//        int pageSize = 2; // Số lượng phim trên mỗi trang
//
//        // Tìm kiếm phim với từ khoá
//        Page<Movie> moviePage = movieRepository.findByKeyword(keyword, PageRequest.of(page, pageSize));
//
//        model.addAttribute("movies", moviePage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", moviePage.getTotalPages());
//        model.addAttribute("keyword", keyword); // Truyền từ khoá tìm kiếm để hiển thị lại trong form
//
//        return "movie/index"; // Trả về view hiển thị danh sách phim
//    }
//@GetMapping
//public String getAllMovies(Model model,
//                           @RequestParam(name = "page", defaultValue = "0") int page,
//                           @RequestParam(name = "size", defaultValue = "2") int size,
//                           @RequestParam(name = "keyword", required = false) String keyword) {
//    Page<Movie> moviePage;
//    if (keyword != null && !keyword.isEmpty()) {
//        moviePage = movieService.searchMovies(keyword, PageRequest.of(page, size));
//        model.addAttribute("keyword", keyword);
//    } else {
//        moviePage = movieService.getAllMovies(PageRequest.of(page, size));
//    }
//    model.addAttribute("movies", moviePage);
//    model.addAttribute("currentPage", page);
//    model.addAttribute("totalPages", moviePage.getTotalPages());
//    return "movie/index";
//}
    @GetMapping("/new")
    public String createMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "movie/movie_form";
    }

    //    @PostMapping
//    public String saveMovie(@ModelAttribute Movie movie) {
//        movieService.saveMovie(movie);
//        return "redirect:/movies";
//    }
    @PostMapping
    public String saveMovie(@ModelAttribute Movie movie,
                            @RequestParam("file") MultipartFile file)throws IOException{
        String imageUrl = saveImage(file); // Lưu hình ảnh và nhận URL
        movie.setImageUrl(imageUrl); // Thiết lập URL cho sản phẩm
        movieService.addMovie(movie,file);
        movieService.saveMovie(movie);
        return "redirect:/movies";
    }

    @GetMapping("/detail/{id}")
    public String showMovieDetail(@PathVariable("id") Long id, Model model) {
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie ID: " + id));
        List<Comment> comments = commentService.findAllByMovieId(id); // Lấy danh sách bình luận cho phim này
        model.addAttribute("movie", movie);
        model.addAttribute("comments", comments); // Thêm danh sách bình luận vào model
        model.addAttribute("newComment", new Comment()); // Đối tượng để nhập bình luận mới
        return "movie/movie_detail";
    }

    @PostMapping("/addComment/{movieId}")
    public String addComment(@PathVariable Long movieId, @ModelAttribute Comment newComment, Authentication authentication) {
        newComment.setCreatedAt(LocalDateTime.now()); // Thiết lập thời gian hiện tại cho bình luận mới

        // Lấy username của người dùng hiện tại từ Authentication
        String username = authentication.getName();
        newComment.setUsername(username); // Gán username của người dùng hiện tại cho bình luận mới

        Movie movie = movieService.getMovieById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie ID: " + movieId));
        newComment.setMovie(movie); // Gán phim cho bình luận mới

        commentService.saveComment(newComment); // Lưu bình luận vào cơ sở dữ liệu

        return "redirect:/movies/detail/{movieId}";
    }

    @PostMapping("/deleteComment/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        Long movieId = commentService.getMovieIdByCommentId(commentId); // Lấy movieId từ commentId
        commentService.deleteComment(commentId); // Xóa bình luận từ cơ sở dữ liệu
        // Redirect back to movie detail page with movieId
        return "redirect:/movies/detail/" + movieId;
    }
    @GetMapping("/edit/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid movie ID: " + id));
        model.addAttribute("movie", movie);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "movie/update_movie";
    }

    //    @PostMapping("/update/{id}")
//    public String updateMovie(@PathVariable Long id, @Valid @ModelAttribute("movie") Movie movie,
//                              BindingResult result) {
//        if (result.hasErrors()) {
//
//            movie.setId(id); // set id to keep it in the form in case of errors
//            return "movie/update_movie";
//        }
//
//        movieService.saveMovie(movie);
//        return "redirect:/movies/detail/" + id;
//    }
    @PostMapping("/update/{id}")
    public String updateMovie(@PathVariable Long id, @Valid @ModelAttribute("movie") Movie movie,
                              BindingResult result, @RequestParam("file") MultipartFile file) throws IOException {
        if (result.hasErrors()) {
            movie.setId(id); // set id to keep it in the form in case of errors
            return "movie/update_movie";
        }

        // Save the movie (without image update)
        movieService.saveMovie(movie);

        // Update the movie image
        movieService.updateMovieImage(id, file);

        return "redirect:/movies/detail/" + id;
    }
    @PostMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id) {
        if (movieService.getMovieById(id).isPresent()) {
            movieService.deleteMovie(id);
        } else {
            throw new IllegalArgumentException("Invalid movie ID: " + id);
        }
        return "redirect:/movies";
    }

    //    @GetMapping("/search")
//    public String searchMovies(@RequestParam("keyword") String keyword, Model model) {
//        List<Movie> searchResults = movieService.searchByKeyword(keyword);
//
//        model.addAttribute("movies", searchResults);
//        model.addAttribute("keyword", keyword); // Truyền từ khóa tìm kiếm để hiển thị trên view
//        return "movie/index"; // Trả về view template hiển thị kết quả tìm kiếm
//    }
//@GetMapping("/search")
//public String searchMovies(@RequestParam("keyword") String keyword,
//                           @RequestParam(name = "page", defaultValue = "0") int page,
//                           @RequestParam(name = "size", defaultValue = "10") int size,
//                           Model model) {
//    Page<Movie> searchResults = movieService.searchByKeyword(keyword, PageRequest.of(page, size));
//
//    model.addAttribute("movies", searchResults);
//    model.addAttribute("keyword", keyword);
//    model.addAttribute("currentPage", page);
//    model.addAttribute("totalPages", searchResults.getTotalPages());
//    return "movie/index"; // Trả về view template hiển thị kết quả tìm kiếm
//}
    // Method to save image and return its URL
    private String saveImage(MultipartFile imageFile) throws IOException {
        // Implement your logic to save image file and return its URL
        return "/images/" + imageFile.getOriginalFilename(); // Example URL
    }
}