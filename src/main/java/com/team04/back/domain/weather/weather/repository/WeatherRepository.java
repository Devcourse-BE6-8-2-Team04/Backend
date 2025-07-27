package com.team04.back.domain.weather.weather.repository;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherInfo, Integer> {
    List<WeatherInfo> findByLocationAndDateBetween(String location, LocalDate start, LocalDate end);
}
