package com.team04.back.domain.comment.comment.entity;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Comment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column()
    private String imageUrl;

    @Column(nullable = false)
    private String sentence;

    @Column(nullable = false)
    private String tagString;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weather_info_id", nullable = false)
    private WeatherInfo weatherInfo;

    public Comment(String email, String password, String imageUrl, String sentence, String tagString, WeatherInfo weatherInfo) {
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.sentence = sentence;
        this.tagString = tagString;
        this.weatherInfo = weatherInfo;
    }
}
