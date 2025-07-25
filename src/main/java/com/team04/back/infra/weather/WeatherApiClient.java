package com.team04.back.infra.weather;

import com.team04.back.infra.weather.dto.OneCallApiResponse;
import com.team04.back.infra.weather.dto.TimeMachineApiResponse;
import com.team04.back.infra.weather.dto.DaySummaryApiResponse;
import com.team04.back.infra.weather.dto.WeatherOverviewApiResponse;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

import java.util.List;

public interface WeatherApiClient {

    /**
     * Current and forecasts weather data.
     * 현재 및 예보 날씨 데이터를 가져옵니다.
     * @param lat 위도, 십진수 (-90; 90). (필수)
     * @param lon 경도, 십진수 (-180; 180). (필수)
     * @param exclude API 응답에서 제외할 날씨 데이터 부분 (예: ["current", "minutely"]). (선택 사항)
     * @param units 측정 단위. "standard", "metric", "imperial" 사용 가능. (선택 사항)
     * @param lang 출력 언어. (선택 사항)
     * @return 구조화된 DTO로 날씨 데이터를 포함하는 Mono.
     */
    Mono<OneCallApiResponse> fetchOneCallWeatherData(@NotNull double lat, @NotNull double lon, List<String> exclude, String units, String lang);

    /**
     * Weather data for timestamp.
     * 특정 타임스탬프에 대한 날씨 데이터를 가져옵니다.
     * @param lat 위도, 십진수 (-90; 90). (필수)
     * @param lon 경도, 십진수 (-180; 180). (필수)
     * @param dt 타임스탬프 (Unix 시간, UTC 시간대). (필수)
     * @param units 측정 단위. "standard", "metric", "imperial" 사용 가능. (선택 사항)
     * @param lang 출력 언어. (선택 사항)
     * @return 구조화된 DTO로 날씨 데이터를 포함하는 Mono.
     */
    Mono<TimeMachineApiResponse> fetchTimeMachineWeatherData(@NotNull double lat, @NotNull double lon, @NotNull long dt, String units, String lang);

    /**
     * Daily Aggregation.
     * 특정 날짜에 대한 집계된 날씨 데이터를 가져옵니다.
     * @param lat 위도, 십진수 (-90; 90). (필수)
     * @param lon 경도, 십진수 (-180; 180). (필수)
     * @param date YYYY-MM-DD 형식의 날짜. (필수)
     * @param tz 시간대 (±XX:XX 형식). (선택 사항)
     * @param units 측정 단위. "standard", "metric", "imperial" 사용 가능. (선택 사항)
     * @return 구조화된 DTO로 일별 요약 날씨 데이터를 포함하는 Mono.
     */
    Mono<DaySummaryApiResponse> fetchDaySummaryWeatherData(@NotNull double lat, @NotNull double lon, @NotNull String date, String tz, String units);

    /**
     * Weather overview
     * 사람이 읽을 수 있는 형태의 날씨 요약을 가져옵니다.
     * @param lat 위도, 십진수 (-90; 90). (필수)
     * @param lon 경도, 십진수 (-180; 180). (필수)
     * @param date YYYY-MM-DD 형식의 날짜. (선택 사항)
     * @param units 측정 단위. "standard", "metric", "imperial" 사용 가능. (선택 사항)
     * @return 구조화된 DTO로 날씨 개요를 포함하는 Mono.
     */
    Mono<WeatherOverviewApiResponse> fetchWeatherOverview(@NotNull double lat, @NotNull double lon, String date, String units);

}
