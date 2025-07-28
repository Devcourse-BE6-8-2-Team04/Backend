package com.team04.back.domain.cloth.cloth.service;

import com.team04.back.domain.cloth.cloth.entity.Clothing;
import com.team04.back.domain.cloth.cloth.entity.ClothInfo;
import com.team04.back.domain.cloth.cloth.entity.ExtraCloth;
import com.team04.back.domain.cloth.cloth.enums.Category;
import com.team04.back.domain.cloth.cloth.repository.ClothRepository;
import com.team04.back.domain.cloth.cloth.repository.ExtraClothRepository;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClothService {
    private final ClothRepository clothRepository;
    private final ExtraClothRepository extraClothRepository;

    public Map<Category, List<Clothing>> getOutfitWithPeriod(List<WeatherInfo> weatherPlan) {
        Map<Category, List<Clothing>> recommendedClothesMap = new HashMap<>();

        for (WeatherInfo weather : weatherPlan) {
            Double feelsLike = weather.getFeelsLikeTemperature();

            List<ClothInfo> recommendedClothes = clothRepository.findByTemperature(feelsLike);

            for (ClothInfo cloth : recommendedClothes) {
                recommendedClothesMap
                        .computeIfAbsent(cloth.getCategory(), k -> new ArrayList<>())
                        .add(cloth);
            }

            getExtraClothes(weather)
                    .forEach(extraCloth -> {
                        recommendedClothesMap
                                .computeIfAbsent(Category.EXTRA, k -> new ArrayList<>())
                                .add(extraCloth);
                    });
        }
        return recommendedClothesMap;
    }

    private Set<ExtraCloth> getExtraClothes(WeatherInfo weather) {
        Set<ExtraCloth> extraCloths = extraClothRepository.findDistinctByWeather(weather.getWeather());
        return extraCloths;
    }

}
