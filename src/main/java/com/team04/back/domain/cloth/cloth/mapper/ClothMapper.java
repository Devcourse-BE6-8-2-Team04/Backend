package com.team04.back.domain.cloth.cloth.mapper;

import com.team04.back.domain.cloth.cloth.dto.CategoryClothDto;
import com.team04.back.domain.cloth.cloth.enums.Category;

public class ClothMapper {

    public static CategoryClothDto toCategoryClothDto(String clothName, String imageUrl, Category category) {
        return new CategoryClothDto(clothName, imageUrl, category);
    }

}
