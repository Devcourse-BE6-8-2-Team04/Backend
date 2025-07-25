package com.team04.back.domain.cloth.cloth.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WeatherAndClothDto {
    private String weather;
    private Double feelsLikeTemperature;
    private Double maxTemperature;
    private Double minTemperature;
    private String location;
    private LocalDate date;
    private LocalDateTime modifyDate;

    public WeatherAndClothDto(String weather, Double feelsLikeTemperature, Double maxTemperature, Double minTemperature, String location, LocalDate date, LocalDateTime modifyDate) {
        this.weather = weather;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.location = location;
        this.date = date;
        this.modifyDate = modifyDate;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Double getFeelsLikeTemperature() {
        return feelsLikeTemperature;
    }

    public void setFeelsLikeTemperature(Double feelsLikeTemperature) {
        this.feelsLikeTemperature = feelsLikeTemperature;
    }

    public Double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(Double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public Double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(Double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(LocalDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }
}
