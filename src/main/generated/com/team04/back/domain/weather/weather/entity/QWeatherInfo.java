package com.team04.back.domain.weather.weather.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWeatherInfo is a Querydsl query type for WeatherInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWeatherInfo extends EntityPathBase<WeatherInfo> {

    private static final long serialVersionUID = -1261669266L;

    public static final QWeatherInfo weatherInfo = new QWeatherInfo("weatherInfo");

    public final DateTimePath<java.time.LocalDateTime> createDate = createDateTime("createDate", java.time.LocalDateTime.class);

    public final NumberPath<Double> dailyTemperatureGap = createNumber("dailyTemperatureGap", Double.class);

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final StringPath description = createString("description");

    public final NumberPath<Double> feelsLikeTemperature = createNumber("feelsLikeTemperature", Double.class);

    public final NumberPath<Integer> humidity = createNumber("humidity", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath location = createString("location");

    public final NumberPath<Double> maxTemperature = createNumber("maxTemperature", Double.class);

    public final NumberPath<Double> minTemperature = createNumber("minTemperature", Double.class);

    public final DateTimePath<java.time.LocalDateTime> modifyDate = createDateTime("modifyDate", java.time.LocalDateTime.class);

    public final NumberPath<Double> pop = createNumber("pop", Double.class);

    public final NumberPath<Double> rain = createNumber("rain", Double.class);

    public final NumberPath<Double> snow = createNumber("snow", Double.class);

    public final NumberPath<Double> uvi = createNumber("uvi", Double.class);

    public final EnumPath<com.team04.back.domain.weather.weather.enums.Weather> weather = createEnum("weather", com.team04.back.domain.weather.weather.enums.Weather.class);

    public final NumberPath<Integer> windDeg = createNumber("windDeg", Integer.class);

    public final NumberPath<Double> windSpeed = createNumber("windSpeed", Double.class);

    public QWeatherInfo(String variable) {
        super(WeatherInfo.class, forVariable(variable));
    }

    public QWeatherInfo(Path<? extends WeatherInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWeatherInfo(PathMetadata metadata) {
        super(WeatherInfo.class, metadata);
    }

}

