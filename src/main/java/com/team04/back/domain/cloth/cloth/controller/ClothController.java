package com.team04.back.domain.cloth.cloth.controller;

import com.team04.back.domain.cloth.cloth.dto.CategoryClothDto;
import com.team04.back.domain.cloth.cloth.dto.WeatherAndClothDto;
import com.team04.back.domain.cloth.cloth.service.ClothService;
import com.team04.back.infra.weather.WeatherApiClient;
import com.team04.back.infra.weather.dto.GeoReverseResponse;
import com.team04.back.infra.weather.dto.OneCallApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/weather/clothes")
public class ClothController {

    private final WeatherApiClient weatherApiClient;
    private final ClothService clothService;
    public ClothController(WeatherApiClient weatherApiClient, ClothService clothService) {
        this.weatherApiClient = weatherApiClient;
        this.clothService = clothService;
    }

    @GetMapping("/details")
    public WeatherAndClothDto getClothDetails(@RequestParam(name = "latitude") double latitude, @RequestParam(name = "longitude") double longitude) {
        // 좌표를 통핸 도시이름 요청
        Mono<List<GeoReverseResponse>> cityResponse = weatherApiClient.fetchCityByCoordinates(latitude, longitude, 1);

        // 도시이름 저장
        String cityName = cityResponse.block().get(0).getName();

        //도시 이름을 기반으로 날씨 정보 가져오기
        Mono<OneCallApiResponse> weatherResponse = weatherApiClient.fetchOneCallWeatherData(
                latitude,
                longitude,
                List.of("current","daly","hourly"),
                "metric",
                "kr"
        );

        weatherResponse.subscribe(
                response -> System.out.println("Weather Data: " + response),
                error -> System.out.println("Error fetching weather data: " + error.getMessage())
        );
        // 날씨 정보에 따라 옷 정보 가져오기
        List<CategoryClothDto> cloths = clothService.findClothByWeather(weatherResponse.block().getCurrent().getFeelsLike());


        // 날씨 정보를  WeatherAndClothDto 생성
        WeatherAndClothDto weatherAndClothDto = new WeatherAndClothDto(
                weatherResponse.block().getCurrent().getWeather().get(0).getMain(),
                weatherResponse.block().getCurrent().getFeelsLike(),
                weatherResponse.block().getDaily().get(0).getTemp().getMax(),
                weatherResponse.block().getDaily().get(0).getTemp().getMin(),
                cityName,
                LocalDateTime.now().toLocalDate(),
                LocalDateTime.now()
        );





        return weatherAndClothDto;
    }






}
