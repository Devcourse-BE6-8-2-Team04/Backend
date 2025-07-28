package com.team04.back.domain.weather.weather.dto;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.enums.Weather;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

public record WeatherInfoDto(
        @NonNull Weather weather,
        @NonNull Double dailyTemperatureGap,
        @NonNull Double feelsLikeTemperature,
        @NonNull Double maxTemperature,
        @NonNull Double minTemperature,
        @NonNull String location,
        @NonNull LocalDate date
){
    public WeatherInfoDto(WeatherInfo weatherInfo) {
        this(
                weatherInfo.getWeather(),
                weatherInfo.getDailyTemperatureGap(),
                weatherInfo.getFeelsLikeTemperature(),
                weatherInfo.getMaxTemperature(),
                weatherInfo.getMinTemperature(),
                weatherInfo.getLocation(),
                weatherInfo.getDate()
        );
    }
}
