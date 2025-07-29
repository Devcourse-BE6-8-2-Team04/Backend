package com.team04.back.domain.comment.comment.service;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Comment> findByLocationAndDate(String location, LocalDate date, Pageable pageable) {
        int month = date.getMonthValue();
        return commentRepository.findByWeatherInfoLocationAndMonth(location, month, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Comment> findByLocationAndTemperature(String location, Double feelsLikeTemperature, Pageable pageable) {
        double minTemperature = feelsLikeTemperature - 2.5;
        double maxTemperature = feelsLikeTemperature + 2.5;
        return commentRepository.findByWeatherInfoLocationAndFeelsLikeTemperature(location, minTemperature, maxTemperature, pageable);
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public long count() {
        return commentRepository.count();
    }

    public Optional<Comment> findById(int id) {
        return commentRepository.findById(id);
    }
}
