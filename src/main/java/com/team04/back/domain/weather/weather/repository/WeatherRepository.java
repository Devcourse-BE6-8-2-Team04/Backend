package com.team04.back.domain.weather.weather.repository;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherInfo, Integer> {
    Optional<WeatherInfo> findByLocation(String location);
}
