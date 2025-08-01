package com.team04.back.domain.comment.comment.dto;

import java.time.LocalDate;

public record CommentSearchDto (
    String location,
    LocalDate date,
    Double feelsLikeTemperature,
    Integer month,
    String email
) {
    public CommentSearchDto(
            String location,
            LocalDate date,
            Double feelsLikeTemperature,
            Integer month,
            String email
    ) {
        this.location = location;
        this.date = date;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.month = month;
        this.email = email;
    }
}