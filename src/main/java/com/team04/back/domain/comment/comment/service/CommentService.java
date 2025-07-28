package com.team04.back.domain.comment.comment.service;

import com.team04.back.domain.comment.comment.dto.CommentDto;
import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.repository.CommentRepository;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Page<CommentDto> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable)
                .map(comment -> {
                    WeatherInfo info = comment.getWeatherInfo();
                    Hibernate.initialize(info);
                    return new CommentDto(comment);
                });
    }

    @Transactional(readOnly = true)
    public Page<CommentDto> findByLocationAndDate(String location, LocalDate date, Pageable pageable) {
        int month = date.getMonthValue();
        return commentRepository.findByWeatherInfoLocationAndMonth(location, month, pageable)
                .map(comment -> {
                    WeatherInfo info = comment.getWeatherInfo();
                    Hibernate.initialize(info);
                    return new CommentDto(comment);
                });
    }

    @Transactional(readOnly = true)
    public Page<CommentDto> findByLocationAndTemperature(String location, Double feelsLikeTemperature, Pageable pageable) {
        double minTemperature = feelsLikeTemperature - 2.5;
        double maxTemperature = feelsLikeTemperature + 2.5;
        return commentRepository.findByWeatherInfoLocationAndFeelsLikeTemperature(location, minTemperature, maxTemperature, pageable)
                .map(comment -> {
                    WeatherInfo info = comment.getWeatherInfo();
                    Hibernate.initialize(info);
                    return new CommentDto(comment);
                });
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public long count() {
        return commentRepository.count();
    }
}
