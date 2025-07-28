package com.team04.back.domain.cloth.cloth.controller;

import com.team04.back.domain.cloth.cloth.dto.WeatherAndClothDto;
import com.team04.back.infra.weather.WeatherApiClient;
import com.team04.back.infra.weather.dto.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClothControllerTest {

    // WeaherAPiClient를 Mock 객체로 생성
    @Mock
    private WeatherApiClient weatherApiClient;


    // Mock 데이터 설정
    private final String cityName = "Seoul";
    private final double latitude = 37.5665;
    private final double longitude = 126.9780;

    @Test
    void getWeatherAndClothTest() {
        // 도시 이름을 가져오는 부분 Mock
        GeoReverseResponse geoResponse = new GeoReverseResponse();
        geoResponse.setName(cityName);
        Mono<List<GeoReverseResponse>> geoResponseMono = Mono.just(Collections.singletonList(geoResponse));

        // 날씨 설명 Mock
        WeatherDescription weatherDescription = new WeatherDescription(1, "Clear", "clear sky", "01d");

        // 현재 날씨 Mock
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setFeelsLike(25);
        currentWeather.setWeather(Collections.singletonList(weatherDescription));

        // DailyTemp 설정
        DailyTemp dailyTemp = new DailyTemp(10, 5, 15, 5, 13, 9);
        DailyData dailyData = new DailyData();
        dailyData.setDt(1625678400L);
        dailyData.setTemp(dailyTemp);

        // OneCallApiResponse를 Mock
        OneCallApiResponse oneCallApiResponse = new OneCallApiResponse(
                latitude,
                longitude,
                "Asia/Seoul",
                32400,
                currentWeather,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.singletonList(dailyData),
                Collections.emptyList()
        );

        // WeatherApiClient 응답 mock 설정
        when(weatherApiClient.fetchCityByCoordinates(latitude, longitude, 1)).thenReturn(geoResponseMono);
        when(weatherApiClient.fetchOneCallWeatherData(latitude, longitude, Collections.singletonList("current"), "metric", "kr"))
                .thenReturn(Mono.just(oneCallApiResponse));

        // 도시 이름 + 날씨 정보로  WeatherAndClothDto 생성
        WeatherAndClothDto weatherAndClothDto = new WeatherAndClothDto(
                oneCallApiResponse.getCurrent().getWeather().get(0).getMain(),
                oneCallApiResponse.getCurrent().getFeelsLike(),
                oneCallApiResponse.getDaily().get(0).getTemp().getMax(),
                oneCallApiResponse.getDaily().get(0).getTemp().getMin(),
                geoResponse.getName(),
                LocalDate.now(),
                LocalDateTime.now()
        );

        // DTO를 검증
        Mono<WeatherAndClothDto> result = Mono.just(weatherAndClothDto);

        // 결과 출력
        result.subscribe(weather -> System.out.println("Weather and Cloth: " + weather));
        result.block();
    }
}
