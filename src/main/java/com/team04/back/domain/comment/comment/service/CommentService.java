package com.team04.back.domain.comment.comment.service;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.repository.CommentRepository;
import com.team04.back.domain.comment.commentSearch.commentSearchCriteria.CommentSearchCriteria;
import com.team04.back.domain.comment.commentSearch.commentSpecification.CommentSpecification;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public long count() {
        return commentRepository.count();
    }

    public Optional<Comment> findById(int id) {
        return commentRepository.findById(id);
    }

    public Page<Comment> findByCriteria(CommentSearchCriteria criteria, Pageable pageable) {
        return commentRepository.findAll(
                CommentSpecification.withCriteria(criteria),
                pageable
        );
    }

    public boolean verifyPassword(Comment comment, String password) {
        return comment.getPassword().equals(password);
    }

    public void delete(Comment comment) {
        commentRepository.delete(comment);
    }

    public Comment createComment(String email, String password, String imageUrl, String title, String sentence, String tagString, WeatherInfo weatherInfo) {
        Comment comment = new Comment(email, password, imageUrl, title, sentence, tagString, weatherInfo);
        return commentRepository.save(comment);
    }

    public Optional<Comment> findLatest() {
        return commentRepository.findFirstByOrderByIdDesc();
    }
}
