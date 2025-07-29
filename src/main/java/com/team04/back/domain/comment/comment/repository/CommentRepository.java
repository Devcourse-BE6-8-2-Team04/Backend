package com.team04.back.domain.comment.comment.repository;

import com.team04.back.domain.comment.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Page<Comment> findAll(Pageable pageable);

    @Query("""
            SELECT c FROM Comment c
            WHERE c.weatherInfo.location = :location
            AND EXTRACT(MONTH FROM c.weatherInfo.date) = :month
            """)
    Page<Comment> findByWeatherInfoLocationAndMonth(
            @Param("location") String location,
            @Param("month") int month,
            Pageable pageable
    );

    @Query("""
            SELECT c FROM Comment c
            WHERE c.weatherInfo.location = :location
            AND c.weatherInfo.feelsLikeTemperature BETWEEN :minTemperature AND :maxTemperature
            """)
    Page<Comment> findByWeatherInfoLocationAndFeelsLikeTemperature(
            @Param("location") String location,
            @Param("minTemperature") double minTemperature,
            @Param("maxTemperature") double maxTemperature,
            Pageable pageable
    );

    @Query("""
            SELECT c FROM Comment c
            WHERE c.weatherInfo.location LIKE CONCAT('%', :location, '%')
            AND EXTRACT(MONTH FROM c.weatherInfo.date) = :month
            AND c.weatherInfo.feelsLikeTemperature BETWEEN :minTemperature AND :maxTemperature
            """)
    Page<Comment> findByWeatherInfoLocationAndMonthAndFeelsLikeTemperature(
            @Param("location") String location,
            @Param("month") int month,
            @Param("minTemperature") double minTemperature,
            @Param("maxTemperature") double maxTemperature,
            Pageable pageable
    );
}
