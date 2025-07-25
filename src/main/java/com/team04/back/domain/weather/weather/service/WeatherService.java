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

    /**
     * 좌표와 날짜를 이용하여 날씨 정보 단건을 조회합니다.
     * @param lat 위도
     * @param lon 경도
     * @param date 조회할 날짜
     * @return 해당 좌표와 날짜에 대한 날씨 정보
     */
    @Transactional
    public WeatherInfo getWeatherInfo(double lat, double lon, LocalDate date) {
        String location = getLocationFromCoordinates(lat, lon);
        return getWeatherInfo(location, lat, lon, date);
    }

    /**
     * 지역 이름과 날짜를 이용하여 날씨 정보 단건을 조회합니다.
     * @param location 지역 이름
     * @param lat 위도
     * @param lon 경도
     * @param date 조회할 날짜
     * @return 해당 지역과 날짜에 대한 날씨 정보
     */
    @Transactional
    public WeatherInfo getWeatherInfo(String location, double lat, double lon, LocalDate date) {
        Optional<WeatherInfo> weatherInfoOpt = weatherRepository.findByLocationAndDate(location, date);

        // 조회 결과가 있고 유효한 경우
        if (weatherInfoOpt.isPresent() && isValid(weatherInfoOpt.get())) {
            return weatherInfoOpt.get();
        }
        // 조회 결과가 없거나 유효하지 않은 경우
        WeatherInfo info = weatherInfoOpt.orElseGet(WeatherInfo::new);
        return updateWeatherInfo(info, location, lat, lon, date);
    }

    /**
     * 좌표와 날짜 범위를 이용하여 날씨 정보 리스트를 조회합니다.
     * @param lat 위도
     * @param lon 경도
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 좌표와 날짜 범위에 대한 날씨 정보 리스트
     */
    @Transactional
    public List<WeatherInfo> getWeatherInfos(double lat, double lon, LocalDate startDate, LocalDate endDate) {
        // 시작 날짜와 종료 날짜 유효성 검사
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("시작 날짜(" + startDate + ")는 종료 날짜(" + endDate + ")보다 이후일 수 없습니다.");
        }

        // 날짜 범위 순회하며 데이터 수집
        List<WeatherInfo> result = new java.util.ArrayList<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            WeatherInfo info = getWeatherInfo(lat, lon, date);
            result.add(info);
            date = date.plusDays(1);
        }
        return result;
    }

    /**
     * 좌표를 이용하여 해당 위치의 이름을 조회합니다.
     * @param lat 위도
     * @param lon 경도
     * @return 해당 좌표에 대한 지역 이름
     */
    private String getLocationFromCoordinates(double lat, double lon) {
        return weatherApiClient.fetchCityByCoordinates(lat, lon, 1)
                .blockOptional()
                .flatMap(list -> list.stream().findFirst())
                .map(geo -> geo.getLocalNames().getKorean())
                .orElse("알 수 없음");
    }

    /**
     * 날씨 정보가 유효한지 검사합니다.
     * - 마지막 업데이트가 3시간 이내인지 확인
     * @param weatherInfo 날씨 정보
     * @return 유효한 경우 true, 그렇지 않은 경우 false
     */
    private boolean isValid(WeatherInfo weatherInfo) {
        LocalDateTime lastUpdated = weatherInfo.getModifyDate();
        if (lastUpdated == null) return false;
        return lastUpdated.isAfter(LocalDateTime.now().minusHours(3));
    }

    /**
     * 요청된 지역과 날짜의 날씨 정보를 업데이트합니다.
     * @param info 기존 날씨 정보
     * @param location 지역 이름
     * @param lat 위도
     * @param lon 경도
     * @param date 요청된 날짜
     * @return 업데이트된 날씨 정보
     */
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

    /**
     * 예보 API를 이용하여 날씨 정보를 업데이트합니다.
     * @param info 기존 날씨 정보
     * @param location 지역 이름
     * @param lat 위도
     * @param lon 경도
     * @param date 요청된 날짜
     * @return 업데이트된 날씨 정보
     */
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

    /**
     * 일별 날씨 데이터를 이용하여 기존 날씨 정보를 업데이트합니다.
     * @param info 기존 날씨 정보
     * @param data 일별 날씨 데이터
     * @param location 지역 이름
     * @param date 요청된 날짜
     */
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