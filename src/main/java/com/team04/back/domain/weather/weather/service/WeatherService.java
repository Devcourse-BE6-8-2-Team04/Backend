package com.team04.back.domain.weather.weather.service;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;

    public void save(WeatherInfo weather) {
        weatherRepository.save(weather);
    }

    @Transactional
    public WeatherInfo getWeatherInfoByLocationAndDate(double lat, double lon, LocalDateTime date) {
        // 좌표to지역함수(lat, long)로부터 지역 정보 생성
        String location = getLocationFromCoordinates(lat, lon);

        // DB에서 해당 지역의 날씨 정보 조회
        Optional<WeatherInfo> weatherInfoOpt = weatherRepository.findByLocation(location);

        // 조회 결과가 있으면 유효성 검사
        if (weatherInfoOpt.isPresent()) {
            WeatherInfo weatherInfo = weatherInfoOpt.get();

            if (isValid(weatherInfo)) {
                // 유효하면 반환
                return weatherInfo;
            } else {
                // 유효하지 않으면 갱신 후 반환
                return updateWeatherInfo(weatherInfo, location, date);
            }
        }

        // 조회 결과가 없으면 새로 저장 후 반환
        return createWeatherInfo(location, date);
    }

    // 좌표를 기반으로 지역 정보를 생성하는 함수
    private String getLocationFromCoordinates(double lat, double lon) {
        // TODO: 좌표를 기반으로 지역 정보를 생성하는 로직 구현

        return "서울특별시";
    }

    // 유효성 검사
    private boolean isValid(WeatherInfo weatherInfo) {
        // TODO: 유효성 검사 로직 구현
        return true;
    }

    // 날씨 정보 갱신
    private WeatherInfo updateWeatherInfo(WeatherInfo weatherInfo, String location, LocalDateTime date) {
        // TODO: 날씨 정보 갱신 로직 구현

        return weatherRepository.save(weatherInfo);
    }

    // 날씨 정보 생성
    private WeatherInfo createWeatherInfo(String location, LocalDateTime date) {
        // TODO: 날씨 정보 생성 로직 구현

        // 새 날씨 정보 객체 생성
        WeatherInfo newWeatherInfo = new WeatherInfo();

        return weatherRepository.save(newWeatherInfo);
    }
}