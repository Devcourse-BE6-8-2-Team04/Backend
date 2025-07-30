package com.team04.back.domain.cloth.cloth.service;

import com.team04.back.domain.cloth.cloth.dto.CategoryClothDto;
import com.team04.back.domain.cloth.cloth.entity.ClothInfo;
import com.team04.back.domain.cloth.cloth.entity.Clothing;
import com.team04.back.domain.cloth.cloth.entity.ExtraCloth;
import com.team04.back.domain.cloth.cloth.enums.Category;
import com.team04.back.domain.cloth.cloth.repository.ClothRepository;
import com.team04.back.domain.cloth.cloth.repository.ExtraClothRepository;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.enums.Weather;
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

    public List<CategoryClothDto> findClothByWeather(Double feelsLikeTemperature) {
        List<CategoryClothDto> cloths = clothRepository.findByMinFeelsLikeLessThanEqualAndMaxFeelsLikeGreaterThanEqual(feelsLikeTemperature, feelsLikeTemperature);
        return cloths;
    }

    public Map<Category, List<Clothing>> getOutfitWithPeriod(List<WeatherInfo> weatherPlan) {
        Map<Category, List<Clothing>> recommendedClothesMap = new HashMap<>();
        Set<ExtraCloth> allExtraClothes = new HashSet<>();

        for (WeatherInfo weather : weatherPlan) {
            Double feelsLike = weather.getFeelsLikeTemperature();

            List<ClothInfo> recommendedClothes = clothRepository.findByTemperature(feelsLike);

            for (ClothInfo cloth : recommendedClothes) {
                recommendedClothesMap
                        .computeIfAbsent(cloth.getCategory(), k -> new ArrayList<>())
                        .add(cloth);
            }

            allExtraClothes.addAll(getExtraClothes(weather));
        }

        recommendedClothesMap
                .computeIfAbsent(Category.EXTRA, k -> new ArrayList<>())
                .addAll(allExtraClothes);

        return recommendedClothesMap;
    }

    public Set<ExtraCloth> getExtraClothes(WeatherInfo weather) {
        Weather weatherGroup = getWeatherGroup(weather);
        return extraClothRepository.findDistinctByWeather(weatherGroup);

    }

    private Weather getWeatherGroup(WeatherInfo weather) {
        int code = weather.getWeather().getCode();

        // 폭염- 체감기온 30 이상
        if (weather.getFeelsLikeTemperature() != null && 30 <= weather.getFeelsLikeTemperature()) {
            return Weather.HEAT_WAVE;
        }

        // 비 또는 뇌우
        if (200 <= code && code < 400 || 500 <= code && code < 600)
            return Weather.MODERATE_RAIN;
        //눈
        if ( 600 <= code && code < 700)
            return Weather.SNOW;

        // 안개 또는 먼지
        if (700 <= code && code < 800)
            return Weather.MIST;

        // 그외에는 맑은 하늘로 간주
        return Weather.CLEAR_SKY;


    }

}
