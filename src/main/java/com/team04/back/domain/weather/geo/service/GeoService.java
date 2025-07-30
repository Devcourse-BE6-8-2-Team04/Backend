package com.team04.back.domain.weather.geo.service;

import com.team04.back.domain.weather.geo.dto.GeoLocationDto;
import com.team04.back.infra.weather.WeatherApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeoService {
    private final WeatherApiClient weatherApiClient;

    /**
     * 주어진 도시 이름에 대한 지역 정보를 가져옵니다.
     * @param location 도시 이름
     * @return 도시에 대한 지역 정보 리스트
     */
    public List<GeoLocationDto> getGeoLocations(String location) {
        return weatherApiClient.fetchCoordinatesByCity(location, null, null)
                .blockOptional()
                .map(response -> response.stream()
                        .map(GeoLocationDto::new)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }
}
