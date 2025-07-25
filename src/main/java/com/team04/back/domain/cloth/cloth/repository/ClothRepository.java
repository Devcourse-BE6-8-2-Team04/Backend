package com.team04.back.domain.cloth.cloth.repository;

import com.team04.back.domain.cloth.cloth.dto.CategoryClothDto;
import com.team04.back.domain.cloth.cloth.entity.ClothInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClothRepository extends JpaRepository<ClothInfo, Integer> {
    List<CategoryClothDto> findByMinFeelingLessThanEqualAndMaxFeelingGreaterThanEqual(Double min, Double max);
}
