package com.team04.back.domain.comment.comment.controller;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.service.CommentService;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.enums.Weather;
import com.team04.back.domain.weather.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CommentControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CommentService commentService;
    @Autowired
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        WeatherInfo mockWeather = new WeatherInfo(Weather.CLEAR_SKY, 10.0, 20.0, 25.0, 15.0, "서울", LocalDate.of(2022, 1, 1));
        weatherService.save(mockWeather);
        Comment comment = new Comment("email", "password", "imageUrl", "sentence", "tagString", mockWeather);
        commentService.save(comment);
    }

    @Test
    @DisplayName("커멘트 다건 조회")
    public void t1() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments")
                ).andDo(print());

        List<Comment> comments = commentService.findAll();

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(comments.size()));

        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$[%d].weatherInfo.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$[%d].weatherInfo.date".formatted(i)).value(comment.getWeatherInfo().getDate().toString()))
                    .andExpect(jsonPath("$[%d].weatherInfo.feelsLikeTemperature".formatted(i)).value(comment.getWeatherInfo().getFeelsLikeTemperature()));
        }
    }

    @Test
    @DisplayName("커멘트 날짜 기반 검색")
    public void t2() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments")
                                .param("location", "서울")
                                .param("date", "2022-01-01")
                ).andDo(print());

        List<Comment> comments = commentService.findByLocationAndDate("서울", LocalDate.of(2022, 1, 1));

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk());

        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$[%d].weatherInfo.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$[%d].weatherInfo.date".formatted(i)).value(comment.getWeatherInfo().getDate().toString()));
        }
    }

    @Test
    @DisplayName("커멘트 체감온도 기반 검색")
    public void t3() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments")
                                .param("location", "서울")
                                .param("feelsLikeTemperature", "20.0")
                ).andDo(print());

        List<Comment> comments = commentService.findByLocationAndTemperature("서울", 20.0);

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk());

        for (int i = 0; i < comments.size(); i++) {
            Comment comment = comments.get(i);
            resultActions
                    .andExpect(jsonPath("$[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$[%d].weatherInfo.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$[%d].weatherInfo.feelsLikeTemperature".formatted(i)).value(comment.getWeatherInfo().getFeelsLikeTemperature()));
        }
    }
}
