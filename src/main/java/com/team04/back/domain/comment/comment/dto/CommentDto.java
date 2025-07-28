package com.team04.back.domain.comment.comment.dto;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import org.springframework.lang.NonNull;

public record CommentDto (
        @NonNull int id,
        @NonNull String email,
        String imageUrl,
        @NonNull String sentence,
        @NonNull String tagString,
        @NonNull WeatherInfo weatherInfo
){
    public CommentDto(Comment comment){
        this(
                comment.getId(),
                comment.getEmail(),
                comment.getImageUrl(),
                comment.getSentence(),
                comment.getTagString(),
                comment.getWeatherInfo()
        );
    }
}
