package com.team04.back.domain.cloth.cloth.repository;

import com.team04.back.domain.cloth.cloth.entity.ClothInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClothRepository extends JpaRepository<ClothInfo, Integer> {
}
