package com.team04.back.domain.comment.comment.controller;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.service.CommentService;
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

    @Test
    @DisplayName("커멘트 다건 조회")
    public void t1() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments")
                ).andDo(print());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findAll(pageable);
        int size = comments.getContent().size();

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(size));

        for (int i = 0; i < size; i++) {
            Comment comment = comments.getContent().get(size - i - 1); // 역순으로 조회
            resultActions
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.date".formatted(i)).value(comment.getWeatherInfo().getDate().toString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.feelsLikeTemperature".formatted(i)).value(comment.getWeatherInfo().getFeelsLikeTemperature()));
        }
    }

    @Test
    @DisplayName("커멘트 날짜 기반 검색")
    public void t2() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments")
                                .param("location", "일본 삿포로")
                                .param("date", "2025-01-01")
                ).andDo(print());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByLocationAndDate("일본 삿포로", LocalDate.of(2025, 1, 1), pageable);
        int size = comments.getContent().size();

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk());

        for (int i = 0; i < size; i++) {
            Comment comment = comments.getContent().get(size - i - 1); // 역순으로 조회
            resultActions
                    .andExpect(jsonPath("$.content[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.date".formatted(i)).value(comment.getWeatherInfo().getDate().toString()));
        }
    }

    @Test
    @DisplayName("커멘트 체감온도 기반 검색")
    public void t3() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments")
                                .param("location", "일본 삿포로")
                                .param("feelsLikeTemperature", "-4.0")
                ).andDo(print());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByLocationAndTemperature("일본 삿포로", -4.0, pageable);
        int size = comments.getContent().size();

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk());

        for (int i = 0; i < size; i++) {
            Comment comment = comments.getContent().get(size - i - 1); // 역순으로 조회
            resultActions
                    .andExpect(jsonPath("$.content[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.feelsLikeTemperature".formatted(i)).value(comment.getWeatherInfo().getFeelsLikeTemperature()));
        }
    }

    @Test
    @DisplayName("잘못된 파라미터로 인한 예외 처리")
    public void t4() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments")
                                .param("feelsLikeTemperature", "-4.0")
                ).andDo(print());

        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.resultCode").value("400-1"))
                .andExpect(jsonPath("$.msg").value("location 파라미터 없이는 date 또는 feelsLikeTemperature 파라미터를 사용할 수 없습니다."));
    }
}
