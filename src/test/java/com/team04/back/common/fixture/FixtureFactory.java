package com.team04.back.common.fixture;

import com.team04.back.domain.cloth.cloth.entity.ClothInfo;
import com.team04.back.domain.cloth.cloth.enums.Category;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.enums.Weather;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FixtureFactory {

    public static ClothInfo createClothInfo(Category category, double minTemp, double maxTemp) {
        return ClothInfo.create(
            "테스트 의류",
            "test_image.jpg",
            category,
            maxTemp,
            minTemp
        );
    }

    public static WeatherInfo createWeatherInfo(String location, LocalDateTime date, Weather weather,Double feelsLikeTemperature) {
        WeatherInfo weatherInfo = new WeatherInfo();
        weatherInfo.setLocation(location);
        weatherInfo.setDate(date);
        weatherInfo.setWeather(weather);
        weatherInfo.setFeelsLikeTemperature(feelsLikeTemperature);
        return weatherInfo;
    }

    public static WeatherInfo createDefaultWeatherInfo(String location, LocalDateTime date) {
        return createWeatherInfo(location, date, Weather.CLEAR_SKY, 20.0);
    }

    public static List<WeatherInfo> createWeatherInfoList(String location, int futureDays) {
        LocalDate today = LocalDate.now();

        return IntStream.range(0, futureDays)
                .mapToObj(i -> {
                    LocalDateTime dateTime = today.plusDays(i).atTime(12, 0, 0);
                    Weather weather = Weather.values()[i % Weather.values().length];
                    double minTemp = 15.0 + i * 0.1;
                    double maxTemp = 25.0 + i * 0.3;
                    return createWeatherInfo(location, dateTime, weather, (minTemp + maxTemp) / 2);
                })
                .collect(Collectors.toList());
    }
}

