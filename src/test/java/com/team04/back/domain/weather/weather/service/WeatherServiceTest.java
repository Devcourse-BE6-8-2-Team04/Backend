package com.team04.back.domain.weather.weather.service;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.enums.Weather;
import com.team04.back.domain.weather.weather.repository.WeatherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @InjectMocks
    private WeatherService weatherService;

    @Mock
    private WeatherRepository weatherRepository;

    private List<WeatherInfo> weatherInfoList;
    private final String TEST_LOCATION = "Seoul";

    @BeforeEach
    void setUp() {
        weatherInfoList = createWeatherInfoList(TEST_LOCATION, 30);
    }

    private List<WeatherInfo> createWeatherInfoList(String location, int futureDays) {
        LocalDate today = LocalDate.now();

        return IntStream.range(0, futureDays)
                .mapToObj(i -> {
                    LocalDate currentDate = today.plusDays(i);
                    LocalDateTime dateTime = currentDate.atTime(12, 0, 0);

                    WeatherInfo weatherInfo = new WeatherInfo();
                    weatherInfo.setWeather(Weather.values()[i % Weather.values().length]);
                    weatherInfo.setDailyTemperatureGap(10.0 + i * 0.1);
                    weatherInfo.setFeelsLikeTemperature(20.0 + i * 0.2);
                    weatherInfo.setMaxTemperature(25.0 + i * 0.3);
                    weatherInfo.setMinTemperature(15.0 + i * 0.1);
                    weatherInfo.setLocation(location);
                    weatherInfo.setDate(dateTime);
                    return weatherInfo;
                })
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("지역과, 시작, 종료일이 주어지면 시작, 종료일을 포함한 날씨 정보를 반환한다.")
    void getWeatherByDuration_success() {
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(7);

        List<WeatherInfo> expectedWeatherInfos = weatherInfoList.stream()
                .filter(wi -> !wi.getDate().toLocalDate().isBefore(start) && !wi.getDate().toLocalDate().isAfter(end))
                .collect(Collectors.toList());

        when(weatherRepository.findByLocationAndDateBetween(
                eq(TEST_LOCATION),
                eq(start),
                eq(end))
        ).thenReturn(expectedWeatherInfos);

        List<WeatherInfo> result = weatherService.getDurationWeather(TEST_LOCATION, start, end);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(expectedWeatherInfos.size());
        assertThat(result).containsExactlyInAnyOrderElementsOf(expectedWeatherInfos);
    }
}
