package com.team04.back.domain.weather.weather.service;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;

    public List<WeatherInfo> getDurationWeather(String location, LocalDate start, LocalDate end) {
        return weatherRepository.findByLocationAndDateBetween(location, start, end);
    }
}
