//package com.quan.demo.service;
//
//import com.quan.demo.model.Movie;
//import com.quan.demo.repository.MovieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Optional;
//import java.util.List;
//
//
//@Service
//public class MovieService {
//    private static final String UPLOAD_DIR = "uploads/images/";
//    @Autowired
//    private MovieRepository movieRepository;
//    public Page<Movie> findByKeyword(String keyword, Pageable pageable) {
//        return movieRepository.findByKeyword(keyword, pageable);
//    }
//    public Page<Movie> findPaginated(Pageable pageable) {
//        return movieRepository.findAll(pageable);
//    }
//
//    public List<Movie> getAllMovies() {
//        return movieRepository.findAll();
//    }
//
//    public Optional<Movie> getMovieById(Long id) {
//        return movieRepository.findById(id);
//    }
//
//    public Movie saveMovie(Movie movie) {
//        return movieRepository.save(movie);
//    }
//    public void addMovie(Movie movie, MultipartFile file) throws IOException {
//        saveMovieImage(movie, file);
//        movieRepository.save(movie);
//    }
//    public void deleteMovie(Long id) {
//        if (movieRepository.existsById(id)) {
//            movieRepository.deleteById(id);
//        } else {
//            throw new IllegalArgumentException("Invalid movie ID: " + id);
//        }
//    }
//
//    private void saveMovieImage(Movie movie, MultipartFile file) throws IOException {
//        if (file != null && !file.isEmpty()) {
//            // Create upload directory if it doesn't exist
//            Path uploadPath = Paths.get(UPLOAD_DIR);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            // Check if file already exists
//            String fileName = file.getOriginalFilename();
//            Path filePath = uploadPath.resolve(fileName);
//
//            if (Files.exists(filePath)) {
//                // File already exists, reuse the existing file's URL
//                movie.setImageUrl("/images/" + fileName);
//            } else {
//                // Save the new file
//                Files.copy(file.getInputStream(), filePath);
//                movie.setImageUrl("/images/" + fileName);
//            }
//        }
//    }
//    public void updateMovieImage(Long id, MultipartFile file) throws IOException {
//        Optional<Movie> optionalMovie = movieRepository.findById(id);
//        if (optionalMovie.isPresent()) {
//            Movie movie = optionalMovie.get();
//            // Delete old image if exists
//            if (movie.getImageUrl() != null && !movie.getImageUrl().isEmpty()) {
//                deleteMovieImage(movie.getImageUrl());
//            }
//            // Save new image
//            saveMovieImage(movie, file);
//            movieRepository.save(movie);
//        } else {
//            throw new IllegalArgumentException("Movie not found with ID: " + id);
//        }
//    }
//    private void deleteMovieImage(String imageUrl) throws IOException {
//        if (imageUrl != null && !imageUrl.isEmpty()) {
//            Path imagePath = Paths.get(imageUrl.substring(1)); // Remove leading "/"
//            Files.deleteIfExists(imagePath);
//        }
//    }
//}
package com.quan.demo.service;

import com.quan.demo.model.Movie;
import com.quan.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MovieService {
    private static final String UPLOAD_DIR = "uploads/images/";
    @Autowired
    private MovieRepository movieRepository;

    public Page<Movie> findByKeyword(String keyword, Pageable pageable) {
        return movieRepository.findByKeyword(keyword, pageable);
    }
    public Page<Movie> findPaginated(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }
    //    public List<Movie> searchByKeyword(String keyword) {
//        return movieRepository.findByKeyword(keyword);
//    }
    public Page<Movie> searchMovies(String keyword, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return movieRepository.findByKeyword(keyword, pageable);
    }
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
    // Lấy danh sách phim với phân trang
//public Page<Movie> getAllMovies(Pageable pageable) {
//    return movieRepository.findAll(pageable);
//}
    public List<Movie> getAllMoviesbyday() {
        return movieRepository.findAllByOrderByReleaseDateDesc();
    }
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }
    // Add a new product to the database
    public void addMovie(Movie movie, MultipartFile file) throws IOException {
        saveMovieImage(movie, file);
        movieRepository.save(movie);
    }
    //    public void updateMovieImage(Long id, MultipartFile file) throws IOException {
//        Optional<Movie> optionalMovie = movieRepository.findById(id);
//        if (optionalMovie.isPresent()) {
//            Movie movie = optionalMovie.get();
//            // Delete old image if exists
//            if (movie.getImageUrl() != null && !movie.getImageUrl().isEmpty()) {
//                deleteMovieImage(movie.getImageUrl());
//            }
//            // Save new image
//            saveMovieImage(movie, file);
//            movieRepository.save(movie);
//        } else {
//            throw new IllegalArgumentException("Movie not found with ID: " + id);
//        }
//    }
//
    public void deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Invalid movie ID: " + id);
        }
    }
    private void saveMovieImage(Movie movie, MultipartFile file) throws IOException {
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
                movie.setImageUrl("/images/" + fileName);
            } else {
                // Save the new file
                Files.copy(file.getInputStream(), filePath);
                movie.setImageUrl("/images/" + fileName);
            }
        }
    }
    public void updateMovieImage(Long id, MultipartFile file) throws IOException {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            // Delete old image if exists
            if (movie.getImageUrl() != null && !movie.getImageUrl().isEmpty()) {
                deleteMovieImage(movie.getImageUrl());
            }
            // Save new image
            saveMovieImage(movie, file);
            movieRepository.save(movie);
        } else {
            throw new IllegalArgumentException("Movie not found with ID: " + id);
        }
    }
    private void deleteMovieImage(String imageUrl) throws IOException {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Path imagePath = Paths.get(imageUrl.substring(1)); // Remove leading "/"
            Files.deleteIfExists(imagePath);
        }
    }
}
