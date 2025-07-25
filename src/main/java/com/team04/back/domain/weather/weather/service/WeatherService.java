package com.team04.back.domain.weather.weather.service;

import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.enums.Weather;
import com.team04.back.domain.weather.weather.repository.WeatherRepository;
import com.team04.back.infra.weather.WeatherApiClient;
import com.team04.back.infra.weather.dto.DailyData;
import com.team04.back.infra.weather.dto.OneCallApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
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
    public WeatherInfo getWeatherInfoByLocationAndDate(double lat, double lon, LocalDate date) {
        // 좌표로부터 지역 이름 조회
        String location = getLocationFromCoordinates(lat, lon);

        // DB에서 해당 지역의 날씨 정보 조회
        Optional<WeatherInfo> weatherInfoOpt = weatherRepository.findByLocation(location);

        // 조회 결과가 있고 유효한 경우
        if (weatherInfoOpt.isPresent() && isValid(weatherInfoOpt.get())) {
            return weatherInfoOpt.get();
        }
        // 조회 결과가 없거나 유효하지 않은 경우
        WeatherInfo info = weatherInfoOpt.orElseGet(WeatherInfo::new);
        return updateWeatherInfo(info, location, lat, lon, date);
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

    private WeatherInfo updateWeatherInfo(WeatherInfo info, String location, double lat, double lon, LocalDate date) {
        LocalDate today = LocalDate.now();

        // 요청된 날짜가 오늘 이전인 경우
        if (date.isBefore(today)) {
            // 추후 필요 시 과거 날씨 데이터 업데이트 기능 구현
            // return updateFromPastWeatherApi(info, location, lat, lon, date);
            throw new IllegalArgumentException("해당 날짜(" + date + ")에 대한 예보 데이터가 존재하지 않습니다.");
        }
        // 요청된 날짜가 오늘부터 7일 이내인 경우
        else if (!date.isAfter(today.plusDays(7))) {
            return updateFromForecastApi(info, location, lat, lon, date);
        }
        // 요청된 날짜가 오늘 이후 7일 이상인 경우
        else {
            // 추후 필요 시 장기 예보 데이터 업데이트 기능 구현
            // return updateFromLongTermForecastApi(info, location, lat, lon, date);
            throw new IllegalArgumentException("해당 날짜(" + date + ")에 대한 예보 데이터가 존재하지 않습니다.");
        }
    }

    private WeatherInfo updateFromForecastApi(WeatherInfo info, String location, double lat, double lon, LocalDate date) {
        OneCallApiResponse response = weatherApiClient.fetchOneCallWeatherData(
                lat,
                lon,
                List.of("minutely", "hourly", "current", "alerts"),
                "metric",
                "kr"
        ).block(); // 블록킹 호출로 API 응답을 기다림

        DailyData matchedDaily = response.getDaily().stream()
                .filter(d -> LocalDateTime.ofEpochSecond(d.getDt(), 0, ZoneOffset.UTC)
                        .toLocalDate().isEqual(date))
                .findFirst()
                .orElse(null);

        if (matchedDaily == null) {
            throw new IllegalArgumentException("해당 날짜(" + date + ")에 대한 예보 데이터가 존재하지 않습니다.");
        }

        updateWeatherInfoFromDailyData(info, matchedDaily, location, date);
        return weatherRepository.save(info);
    }

    private void updateWeatherInfoFromDailyData(WeatherInfo info, DailyData data, String location, LocalDate date) {
        info.setWeather(Weather.fromCode(data.getWeather().getFirst().getId()));
        info.setDailyTemperatureGap(data.getTemp().getMax() - data.getTemp().getMin());
        info.setFeelsLikeTemperature(data.getFeelsLike().getDay());
        info.setMaxTemperature(data.getTemp().getMax());
        info.setMinTemperature(data.getTemp().getMin());
        info.setLocation(location);
        info.setDate(date);
    }
}