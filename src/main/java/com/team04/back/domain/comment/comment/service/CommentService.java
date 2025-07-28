package com.team04.back.domain.comment.comment.service;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public List<Comment> findByLocationAndDate(String location, LocalDate date) {
        int month = date.getMonthValue();
        return commentRepository.findByWeatherInfoLocationAndMonth(location, month);
    }

    public List<Comment> findByLocationAndTemperature(String location, Double feelsLikeTemperature) {
        double minTemperature = feelsLikeTemperature - 2.5;
        double maxTemperature = feelsLikeTemperature + 2.5;
        return commentRepository.findByWeatherInfoLocationAndFeelsLikeTemperature(location, minTemperature, maxTemperature);
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }
}
