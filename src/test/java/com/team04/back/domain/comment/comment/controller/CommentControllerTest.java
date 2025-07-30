package com.team04.back.domain.comment.comment.controller;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.service.CommentService;
import com.team04.back.domain.comment.commentSearch.commentSearchCriteria.CommentSearchCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .location(null)
                .date(null)
                .feelsLikeTemperature(null)
                .month(null)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByCriteria(criteria, pageable);
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
                    .andExpect(jsonPath("$.content[%d].title".formatted(i)).value(comment.getTitle()))
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
                                .param("location", "삿포로")
                                .param("date", "2025-01-01")
                ).andDo(print());

        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .location("삿포로")
                .date(LocalDate.of(2025, 1, 1))
                .feelsLikeTemperature(null)
                .month(null)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByCriteria(criteria, pageable);
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
                    .andExpect(jsonPath("$.content[%d].title".formatted(i)).value(comment.getTitle()))
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
                                .param("location", "삿포로")
                                .param("feelsLikeTemperature", "-4.0")
                ).andDo(print());

        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .location("삿포로")
                .date(null)
                .feelsLikeTemperature(-4.0)
                .month(null)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByCriteria(criteria, pageable);
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
                    .andExpect(jsonPath("$.content[%d].title".formatted(i)).value(comment.getTitle()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.feelsLikeTemperature".formatted(i)).value(comment.getWeatherInfo().getFeelsLikeTemperature()));
        }
    }

    @Test
    @DisplayName("커멘트 단건 조회")
    public void t4() throws Exception {
        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .location(null)
                .date(null)
                .feelsLikeTemperature(null)
                .month(null)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByCriteria(criteria, pageable);

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
                .andExpect(jsonPath("$.title").value(comment.getTitle()))
                .andExpect(jsonPath("$.sentence").value(comment.getSentence()))
                .andExpect(jsonPath("$.tagString").value(comment.getTagString()))
                .andExpect(jsonPath("$.weatherInfoDto.location").value(comment.getWeatherInfo().getLocation()))
                .andExpect(jsonPath("$.weatherInfoDto.date").value(comment.getWeatherInfo().getDate().toString()))
                .andExpect(jsonPath("$.weatherInfoDto.feelsLikeTemperature").value(comment.getWeatherInfo().getFeelsLikeTemperature()));
    }

    @Test
    @DisplayName("커멘트 단건 조회 - 존재하지 않는 ID")
    public void t5() throws Exception {
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

    @Test
    @DisplayName("커멘트 조건 검색 - 월 필터링")
    public void t6() throws Exception {
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/comments")
                                .param("month", "1") // 1월 필터링
                ).andDo(print());

        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .location(null)
                .date(null)
                .feelsLikeTemperature(null)
                .month(1) // 1월 필터링
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByCriteria(criteria, pageable);
        int size = comments.getContent().size();

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("getComments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(size));

        for (int i = 0; i < size; i++) {
            Comment comment = comments.getContent().get(size - i - 1); // 역순
            resultActions
                    .andExpect(jsonPath("$.content[%d].id".formatted(i)).value(comment.getId()))
                    .andExpect(jsonPath("$.content[%d].email".formatted(i)).value(comment.getEmail()))
                    .andExpect(jsonPath("$.content[%d].imageUrl".formatted(i)).value(comment.getImageUrl()))
                    .andExpect(jsonPath("$.content[%d].title".formatted(i)).value(comment.getTitle()))
                    .andExpect(jsonPath("$.content[%d].sentence".formatted(i)).value(comment.getSentence()))
                    .andExpect(jsonPath("$.content[%d].tagString".formatted(i)).value(comment.getTagString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.location".formatted(i)).value(comment.getWeatherInfo().getLocation()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.date".formatted(i)).value(comment.getWeatherInfo().getDate().toString()))
                    .andExpect(jsonPath("$.content[%d].weatherInfoDto.feelsLikeTemperature".formatted(i)).value(comment.getWeatherInfo().getFeelsLikeTemperature()));
        }
    }

    @Test
    @DisplayName("커멘트 비밀번호 검증")
    public void t7() throws Exception {
        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .location(null)
                .date(null)
                .feelsLikeTemperature(null)
                .month(null)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByCriteria(criteria, pageable);

        int id = comments.getContent().get(0).getId();

        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/comments/" + id + "/verify-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "password": "1234"
                                        }
                                        """)
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("verifyPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("비밀번호가 일치합니다."))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @DisplayName("커멘트 비밀번호 검증 - 잘못된 비밀번호")
    public void t8() throws Exception {
        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .location(null)
                .date(null)
                .feelsLikeTemperature(null)
                .month(null)
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Comment> comments = commentService.findByCriteria(criteria, pageable);

        int id = comments.getContent().get(0).getId();

        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/comments/" + id + "/verify-password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "password": "wrong-password"
                                        }
                                        """)
                ).andDo(print());

        resultActions
                .andExpect(handler().handlerType(CommentController.class))
                .andExpect(handler().methodName("verifyPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("400-1"))
                .andExpect(jsonPath("$.msg").value("비밀번호가 일치하지 않습니다."))
                .andExpect(jsonPath("$.data").value(false));
    }
}
