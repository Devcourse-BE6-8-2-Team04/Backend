package com.team04.back.domain.comment.commentSearch.commentSearchCriteria;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class CommentSearchCriteria {
    private String location;
    private LocalDate date;
    private Double feelsLikeTemperature;
    private Integer month;
    private String email;
    
    public boolean hasLocation() {
        return location != null && !location.trim().isEmpty();
    }
    
    public boolean hasDate() {
        return date != null;
    }
    
    public boolean hasTemperature() {
        return feelsLikeTemperature != null;
    }
    
    public boolean hasMonth() {
        return month != null;
    }
}