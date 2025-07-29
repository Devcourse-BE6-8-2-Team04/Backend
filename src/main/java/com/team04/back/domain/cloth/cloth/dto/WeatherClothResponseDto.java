package com.team04.back.domain.cloth.cloth.dto;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeatherClothResponseDto {
    private WeatherInfo weatherInfo;
    private List<CategoryClothDto>  clothList;

    public WeatherClothResponseDto(WeatherInfo weatherInfo, List<CategoryClothDto> clothList) {
        this.weatherInfo = weatherInfo;
        this.clothList = clothList;
    }
}
