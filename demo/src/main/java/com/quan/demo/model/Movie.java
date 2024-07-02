package com.quan.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String director;

    @Lob // This annotation is needed for LONGTEXT in MySQL
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String imageUrl; // Field for the movie image URL

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private List<Comment> comments;
}
