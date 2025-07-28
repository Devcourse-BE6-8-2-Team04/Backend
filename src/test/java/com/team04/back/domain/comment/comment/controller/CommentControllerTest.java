package com.team04.back.domain.comment.comment.controller;

import com.team04.back.domain.comment.comment.dto.CommentDto;
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
        Page<CommentDto> comments = commentService.findAll(pageable);

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(comments.getContent().size()));

        for (int i = 0; i < comments.getContent().size(); i++) {
            CommentDto commentDto = comments.getContent().get(i);
            resultActions
                    .andExpect(jsonPath("$.content[%d].id".formatted(i)).value(commentDto.id()))
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(commentDto.email()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(commentDto.imageUrl()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(commentDto.sentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(commentDto.tagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.location".formatted(i)).value(commentDto.weatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.date".formatted(i)).value(commentDto.weatherInfo().getDate().toString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.feelsLikeTemperature".formatted(i)).value(commentDto.weatherInfo().getFeelsLikeTemperature()));
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
        Page<CommentDto> comments = commentService.findByLocationAndDate("일본 삿포로", LocalDate.of(2025, 1, 1), pageable);

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk());

        for (int i = 0; i < comments.getContent().size(); i++) {
            CommentDto commentDto = comments.getContent().get(i);
            resultActions
                    .andExpect(jsonPath("$.content[%d].id".formatted(i)).value(commentDto.id()))
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(commentDto.email()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(commentDto.imageUrl()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(commentDto.sentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(commentDto.tagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.location".formatted(i)).value(commentDto.weatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.date".formatted(i)).value(commentDto.weatherInfo().getDate().toString()));
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
        Page<CommentDto> comments = commentService.findByLocationAndTemperature("일본 삿포로", -4.0, pageable);

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk());

        for (int i = 0; i < comments.getContent().size(); i++) {
            CommentDto commentDto = comments.getContent().get(i);
            resultActions
                    .andExpect(jsonPath("$.content[%d].id".formatted(i)).value(commentDto.id()))
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(commentDto.email()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(commentDto.imageUrl()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(commentDto.sentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(commentDto.tagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.location".formatted(i)).value(commentDto.weatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfo.feelsLikeTemperature".formatted(i)).value(commentDto.weatherInfo().getFeelsLikeTemperature()));
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
