package com.quan.demo.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    private String username;

    @Column(columnDefinition = "TEXT")
    private String content;
    private int rating; // Thêm trường rating (điểm đánh giá)
    private LocalDateTime createdAt;

    // Constructor, getters, and setters
}
