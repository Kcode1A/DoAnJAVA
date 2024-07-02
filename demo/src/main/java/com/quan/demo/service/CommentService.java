package com.quan.demo.service;

import com.quan.demo.model.Comment;
import com.quan.demo.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

        public List<Comment> findAllByMovieId(Long movieId) {
        return commentRepository.findAllByMovieId(movieId);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
    public Long getMovieIdByCommentId(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            Comment comment = commentOptional.get();
            return comment.getMovie().getId(); // Assuming you have a method to get Movie from Comment
        }
        throw new IllegalArgumentException("Invalid comment ID: " + commentId);
    }
}
