package com.team04.back.domain.cloth.cloth.controller;

import com.team04.back.domain.cloth.cloth.dto.CategoryClothDto;
import com.team04.back.domain.cloth.cloth.entity.ClothInfo;
import com.team04.back.domain.cloth.cloth.enums.Category;
import com.team04.back.domain.cloth.cloth.repository.ClothRepository;
import com.team04.back.domain.cloth.cloth.service.ClothService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;



@ActiveProfiles("test")
@SpringBootTest
@Transactional
class ClothRepositoryTest {

    @Autowired
    private ClothRepository clothRepository;
    @Autowired
    private ClothService clothService;

    @Test
    void findClothByWeather_withValidFeelsLikeTemp() {

        // 옷 가져오는 기준 - 체감온도
        List<CategoryClothDto> results = clothService.findClothByWeather(15.0);

        // 테스트용 옷 데이터 생성
        ClothInfo cloth1 = new ClothInfo("자켓", "url1", Category.CASUAL_DAILY, 30.0, 20.0); // 제외
        ClothInfo cloth2 = new ClothInfo("패딩", "url2", Category.CASUAL_DAILY, 20.0, 10.0); // 포함
        ClothInfo cloth3 = new ClothInfo("반팔", "url3", Category.CASUAL_DAILY, 10.0, 0.0); // 제외

        clothRepository.save(cloth1);
        clothRepository.save(cloth2);
        clothRepository.save(cloth3);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getClothName()).isEqualTo("패딩");
    }

}