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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
        if(commentService.count() > 0) return;

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

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findAll(pageable);

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(comments.getContent().size()));

        for (int i = 0; i < comments.getContent().size(); i++) {
            Comment comment = comments.getContent().get(i);
            resultActions
                    .andExpect(jsonPath("$.content[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.date".formatted(i)).value(comment.getWeatherInfo().getDate().toString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.feelsLikeTemperature".formatted(i)).value(comment.getWeatherInfo().getFeelsLikeTemperature()));
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

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByLocationAndDate("서울", LocalDate.of(2022, 1, 1), pageable);

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk());

        for (int i = 0; i < comments.getContent().size(); i++) {
            Comment comment = comments.getContent().get(i);
            resultActions
                    .andExpect(jsonPath("$.content[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.date".formatted(i)).value(comment.getWeatherInfo().getDate().toString()));
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

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByLocationAndTemperature("서울", 20.0, pageable);

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk());

        for (int i = 0; i < comments.getContent().size(); i++) {
            Comment comment = comments.getContent().get(i);
            resultActions
                    .andExpect(jsonPath("$.content[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.feelsLikeTemperature".formatted(i)).value(comment.getWeatherInfo().getFeelsLikeTemperature()));
        }
    }

    @Test
    @DisplayName("잘못된 파라미터로 인한 예외 처리")
    public void t4() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments")
                                .param("feelsLikeTemperature", "20.0")
                ).andDo(print());

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("400-1"))
                .andExpect(jsonPath("$.msg").value("location 파라미터 없이는 date 또는 feelsLikeTemperature 파라미터를 사용할 수 없습니다."));
    }

    @Test
    @DisplayName("커멘트 단건 조회")
    public void t5() throws Exception {
        Page<Comment> comments = commentService.findAll(PageRequest.of(0, 10));
        int id = comments.getContent().get(0).getId();

        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments/" + id)
                ).andDo(print());

        Comment comment = commentService.findById(id).get();

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.email").value(comment.getEmail()))
                .andExpect(jsonPath("$.imageUrl").value(comment.getImageUrl()))
                .andExpect(jsonPath("$.sentence").value(comment.getSentence()))
                .andExpect(jsonPath("$.tagString").value(comment.getTagString()))
                .andExpect(jsonPath("$.weatherInfo.location").value(comment.getWeatherInfo().getLocation()))
                .andExpect(jsonPath("$.weatherInfo.date").value(comment.getWeatherInfo().getDate().toString()))
                .andExpect(jsonPath("$.weatherInfo.feelsLikeTemperature").value(comment.getWeatherInfo().getFeelsLikeTemperature()));
    }

    @Test
    @DisplayName("커멘트 단건 조회 - 존재하지 않는 ID")
    public void t6() throws Exception {
        int id = Integer.MAX_VALUE; // 존재하지 않는 ID

        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments/" + id)
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComment"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.resultCode").value("404-1"))
                .andExpect(jsonPath("$.msg").value("해당 데이터가 존재하지 않습니다."));
        }
}
