package com.team04.back.domain.weather.weather.entity;

import com.team04.back.domain.weather.weather.enums.Weather;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WeatherInfo{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 날씨
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Weather weather;

    // 일교차
    @Column(nullable = false)
    private Double dailyTemperatureGap;

    // 체감 온도
    @Column(nullable = false)
    private Double feelsLikeTemperature;

    // 최고 온도
    @Column(nullable = false)
    private Double maxTemperature;

    // 최저 온도
    @Column(nullable = false)
    private Double minTemperature;

    // 지역(지역구 기준)
    @Column(nullable = false)
    private String location;

    // 날짜
    @Column(nullable = false)
    private LocalDate date;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    public WeatherInfo(Weather weather, Double dailyTemperatureGap, Double feelsLikeTemperature, Double maxTemperature, Double minTemperature, String location, LocalDate date) {
        this.weather = weather;
        this.dailyTemperatureGap = dailyTemperatureGap;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.location = location;
        this.date = date;
    }
}