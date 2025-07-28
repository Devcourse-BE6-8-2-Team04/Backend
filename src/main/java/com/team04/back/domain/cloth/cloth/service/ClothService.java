package com.team04.back.domain.cloth.cloth.service;

import com.team04.back.domain.cloth.cloth.entity.ClothInfo;
import com.team04.back.domain.cloth.cloth.enums.Category;
import com.team04.back.domain.cloth.cloth.repository.ClothRepository;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList; // ArrayList import 추가
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClothService {
    private final ClothRepository clothRepository;

    public Map<Category, List<ClothInfo>> getOutfitWithPeriod(List<WeatherInfo> weatherPlan) {
        Map<Category, List<ClothInfo>> recommendedClothesMap = new HashMap<>();

        for (WeatherInfo weather : weatherPlan) {
            Double feelsLike = weather.getFeelsLikeTemperature();

            List<ClothInfo> recommendedClothes = clothRepository.findByTemperature(feelsLike);

            for (ClothInfo cloth : recommendedClothes) {
                recommendedClothesMap
                    .computeIfAbsent(cloth.getCategory(), k -> new ArrayList<>())
                    .add(cloth);
            }
        }
        return recommendedClothesMap;
    }
}
