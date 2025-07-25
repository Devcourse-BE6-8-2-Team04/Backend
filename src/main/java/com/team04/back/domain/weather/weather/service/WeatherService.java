package com.team04.back.domain.weather.weather.service;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.repository.WeatherRepository;
import com.team04.back.infra.weather.WeatherApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherApiClient weatherApiClient;

    public void save(WeatherInfo weather) {
        weatherRepository.save(weather);
    }

    @Transactional
    public WeatherInfo getWeatherInfoByLocationAndDate(double lat, double lon, LocalDateTime date) {
        // 좌표로부터 지역 이름 조회
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

    // 좌표로부터 지역 이름을 가져오는 메서드
    private String getLocationFromCoordinates(double lat, double lon) {
        return weatherApiClient.fetchCityByCoordinates(lat, lon, 1)
                .blockOptional()
                .flatMap(list -> list.stream().findFirst())
                .map(geo -> geo.getLocalNames().getKorean())
                .orElse("알 수 없음");
    }

    // 유효성 검사
    private boolean isValid(WeatherInfo weatherInfo) {
        if (weatherInfo == null) return false;
        LocalDateTime lastUpdated = weatherInfo.getModifyDate();
        if (lastUpdated == null) return false;
        // 마지막 업데이트가 3시간 이내인지 확인
        return lastUpdated.isAfter(LocalDateTime.now().minusHours(3));
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