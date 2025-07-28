package com.team04.back.domain.cloth.cloth.controller;

import com.team04.back.domain.cloth.cloth.dto.OutfitResponse;
import com.team04.back.domain.cloth.cloth.entity.Clothing;
import com.team04.back.domain.cloth.cloth.enums.Category;
import com.team04.back.domain.cloth.cloth.service.ClothService;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.service.WeatherService;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    record TripSchedule(
            @NotBlank
            String place,
            @NotBlank
            @PastOrPresent
            LocalDate start,
            @NotBlank
            @Future
            LocalDate end
    ) {
    }

    @GetMapping
    public OutfitResponse getOutfitWithPeriod(@RequestBody TripSchedule tripSchedule) {
        List<WeatherInfo> duration = weatherService.getDurationWeather(tripSchedule.place, tripSchedule.start, tripSchedule.end);
        Map<Category, List<Clothing>> outfits = clothService.getOutfitWithPeriod(duration);
        return new OutfitResponse(outfits);
    }
}
