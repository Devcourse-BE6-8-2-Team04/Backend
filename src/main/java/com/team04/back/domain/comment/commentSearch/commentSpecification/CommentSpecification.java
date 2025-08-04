package com.team04.back.domain.comment.commentSearch.commentSpecification;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.commentSearch.commentSearchCriteria.CommentSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CommentSpecification {

    public static Specification<Comment> withCriteria(CommentSearchCriteria criteria) {
        return hasLocation(criteria.getLocation())            // 첫 번째 조건부터 시작
                .and(hasDate(criteria.getDate()))             // AND 로 연결
                .and(hasTemperature(criteria.getFeelsLikeTemperature()))  // AND 로 연결
                .and(hasMonth(criteria.getMonth()))           // AND 로 연결
                .and(hasEmail(criteria.getEmail()));          // AND 로 연결
    }
    
    private static Specification<Comment> hasLocation(String location) {
        return (root, query, criteriaBuilder) -> {
            if (location == null || location.trim().isEmpty()) {
                return null; // null을 반환하면 이 조건은 무시됨
            }
            return criteriaBuilder.like(
                    root.get("weatherInfo").get("location"),
                    "%" + location + "%"
            );
        };
    }
    
    private static Specification<Comment> hasDate(LocalDate date) {
        if (date == null) {
            return null; // null을 반환하면 이 조건은 무시됨
        }
        Integer month = date.getMonthValue();
        return hasMonth(month);
    }
    
    private static Specification<Comment> hasTemperature(Double temperature) {
        return (root, query, criteriaBuilder) -> {
            if (temperature == null) {
                return null; // null을 반환하면 이 조건은 무시됨
            }
            double minTemperature = temperature - 2.5;
            double maxTemperature = temperature + 2.5;
            return criteriaBuilder.between(
                    root.get("weatherInfo").get("feelsLikeTemperature"),
                    minTemperature,
                    maxTemperature
            );
        };
    }
    
    private static Specification<Comment> hasMonth(Integer month) {
        return (root, query, criteriaBuilder) -> {
            if (month == null) {
                return null; // null을 반환하면 이 조건은 무시됨
            }
            return criteriaBuilder.equal(
                    criteriaBuilder.function("MONTH", Integer.class, root.get("weatherInfo").get("date")),
                    month
            );
        };
    }

    private static Specification<Comment> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (email == null || email.trim().isEmpty()) {
                return null; // null을 반환하면 이 조건은 무시됨
            }
            return criteriaBuilder.equal(root.get("email"), email);
        };
    }
}