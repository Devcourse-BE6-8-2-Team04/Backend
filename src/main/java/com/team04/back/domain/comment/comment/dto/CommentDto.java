package com.team04.back.domain.comment.comment.dto;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.weather.weather.dto.WeatherInfoDto;
import org.springframework.lang.NonNull;

public record CommentDto (
        @NonNull int id,
        @NonNull String email,
        String imageUrl,
        @NonNull String sentence,
        @NonNull String tagString,
        @NonNull WeatherInfoDto weatherInfoDto
){
    public CommentDto(Comment comment){
        this(
                comment.getId(),
                comment.getEmail(),
                comment.getImageUrl(),
                comment.getSentence(),
                comment.getTagString(),
                new WeatherInfoDto(comment.getWeatherInfo())
        );
    }
}
