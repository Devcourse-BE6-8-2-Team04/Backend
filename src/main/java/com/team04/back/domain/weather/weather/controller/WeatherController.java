package com.team04.back.domain.weather.weather.controller;

import com.team04.back.domain.weather.weather.dto.WeatherInfoDto;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weathers")
@RequiredArgsConstructor
@Tag(name = "Weather", description = "날씨 정보 API")
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping
    @Operation(summary = "주간 날씨 조회", description = "위도와 경도를 이용하여 주간 날씨 정보를 조회합니다.")
    public List<WeatherInfoDto> getWeeklyWeather(
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        LocalDate date = LocalDate.now();
        List<WeatherInfo> weatherInfos = weatherService.getWeatherInfos(lat, lon, date, date.plusDays(6));
        return weatherInfos.stream()
                .map(WeatherInfoDto::new)
                .toList();
    }

    @GetMapping("/{date}")
    @Operation(summary = "특정 날짜 날씨 조회", description = "위도와 경도를 이용하여 특정 날짜의 날씨 정보를 조회합니다.")
    public WeatherInfoDto getWeatherByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam double lat,
            @RequestParam double lon
    ) {
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(lat, lon, date);
        return new WeatherInfoDto(weatherInfo);
    }
}
