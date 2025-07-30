package com.team04.back.domain.cloth.cloth.controller;

import com.team04.back.domain.cloth.cloth.dto.CategoryClothDto;
import com.team04.back.domain.cloth.cloth.dto.WeatherClothResponseDto;
import com.team04.back.domain.cloth.cloth.enums.Category;
import com.team04.back.domain.cloth.cloth.service.ClothService;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.enums.Weather;
import com.team04.back.domain.weather.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ClothControllerUnitTest {

    private static final double TEST_LATITUDE = 37.5;
    private static final double TEST_LONGITUDE = 127.0;

    private static final Weather WEATHER_DESC = Weather.CLEAR_SKY;
    private static final double FEELS_LIKE_TEMP = 23.0;
    private static final double MAX_TEMP = 28.0;
    private static final double MIN_TEMP = 18.0;
    private static final String LOCATION = "서울";

    private static final CategoryClothDto CLOTH_1 = new CategoryClothDto("반팔티", "/images/tshirt.png", Category.CASUAL_DAILY);
    private static final CategoryClothDto CLOTH_2 = new CategoryClothDto("청바지", "/images/jeans.png", Category.CASUAL_DAILY);

    @InjectMocks
    private ClothController clothController;

    @Mock
    private WeatherService weatherService;

    @Mock
    private ClothService clothService;

    @Test
    void getClothDetails_ReturnsExpectedResult() {
        // given
        WeatherInfo mockWeatherInfo = new WeatherInfo();
        mockWeatherInfo.setWeather(WEATHER_DESC);
        mockWeatherInfo.setFeelsLikeTemperature(FEELS_LIKE_TEMP);
        mockWeatherInfo.setMaxTemperature(MAX_TEMP);
        mockWeatherInfo.setMinTemperature(MIN_TEMP);
        mockWeatherInfo.setLocation(LOCATION);

        List<CategoryClothDto> mockCloths = List.of(CLOTH_1, CLOTH_2);

        when(weatherService.getWeatherInfo(anyDouble(), anyDouble(), any()))
                .thenReturn(mockWeatherInfo);

        //현재 체감온도 기준으로
        when(clothService.findClothByWeather(FEELS_LIKE_TEMP))
                .thenReturn(mockCloths);

        // when
        WeatherClothResponseDto response = clothController.getClothDetails(TEST_LATITUDE, TEST_LONGITUDE); // 체감온도 해당하는 옷 정보 가져오기

        // then
        assertThat(response.getWeatherInfo().getWeather()).isEqualTo(WEATHER_DESC);
        assertThat(response.getClothList()).hasSize(2);
        assertThat(response.getClothList().get(0).getClothName()).isEqualTo("반팔티");
    }
}
