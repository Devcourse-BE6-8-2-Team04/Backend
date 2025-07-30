package com.team04.back.domain.cloth.cloth.controller;

import com.team04.back.domain.cloth.cloth.dto.CategoryClothDto;
import com.team04.back.domain.cloth.cloth.dto.OutfitResponse;
import com.team04.back.domain.cloth.cloth.dto.WeatherClothResponseDto;
import com.team04.back.domain.cloth.cloth.entity.Clothing;
import com.team04.back.domain.cloth.cloth.enums.Category;
import com.team04.back.domain.cloth.cloth.service.ClothService;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cloth")
@RequiredArgsConstructor
public class ClothController {
    private final ClothService clothService;
    private final WeatherService weatherService;

    @GetMapping("/details")
    @Operation(summary = "날씨 기반 옷 정보 조회", description = "위도와 경도를 이용하여 날씨 정보를 조회하고, 해당 날씨에 적합한 옷 정보를 반환합니다.")
    public WeatherClothResponseDto getClothDetails(
            @Parameter(description = "위도", example = "37.5")
            @RequestParam(name = "latitude") double latitude,

            @Parameter(description = "경도", example = "127.0")
            @RequestParam(name = "longitude") double longitude) {
        //좌표 기반으로 날씨 정보 가져오기
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(latitude, longitude, LocalDate.now());

        // 날씨 정보에 따라 옷 정보 가져오기
        List<CategoryClothDto> cloths = clothService.findClothByWeather(weatherInfo.getFeelsLikeTemperature());

        // 날씨 정보와 옷 정보를 포함한 응답 DTO 리턴
        return new WeatherClothResponseDto(weatherInfo, cloths);
    }

    record TripSchedule(
            @FutureOrPresent
            LocalDate start,
            @Future
            LocalDate end,
            double lat,
            double lon
    ) {
        public TripSchedule {
            if (start != null && end != null && !start.isBefore(end)) {
                throw new IllegalArgumentException("시작 날짜는 종료 날짜보다 이전이어야 합니다.");
            }
        }
    }

    @GetMapping
    public OutfitResponse getOutfitWithPeriod(TripSchedule tripSchedule) {
        List<WeatherInfo> duration = weatherService.getWeatherInfos(
                tripSchedule.lat,
                tripSchedule.lon,
                tripSchedule.start,
                tripSchedule.end
        );
        Map<Category, List<Clothing>> outfits = clothService.getOutfitWithPeriod(duration);
        return new OutfitResponse(outfits);
    }
}
