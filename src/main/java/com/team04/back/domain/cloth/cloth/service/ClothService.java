package com.team04.back.domain.cloth.cloth.service;

import com.team04.back.domain.cloth.cloth.dto.CategoryClothDto;

import com.team04.back.domain.cloth.cloth.repository.ClothRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClothService {
    private final ClothRepository clothRepository;

    public ClothService(ClothRepository clothRepository) {
        this.clothRepository = clothRepository;
    }

    public List<CategoryClothDto> findClothByWeather(Double feelsLikeTemperature) {
        List<CategoryClothDto> cloths = clothRepository.findByMinFeelingLessThanEqualAndMaxFeelingGreaterThanEqual(feelsLikeTemperature, feelsLikeTemperature);
        return cloths;
    }
}
