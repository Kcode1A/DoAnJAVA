package com.quan.demo.repository;

import com.quan.demo.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findAllByOrderByReleaseDateDesc();
    //
    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Movie> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Movie> findAll(Pageable pageable);
}
