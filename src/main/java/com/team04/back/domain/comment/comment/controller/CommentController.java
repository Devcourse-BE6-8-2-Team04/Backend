package com.team04.back.domain.comment.comment.controller;

import com.team04.back.domain.comment.comment.dto.CommentDto;
import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.service.CommentService;
import com.team04.back.domain.comment.commentSearch.commentSearchCriteria.CommentSearchCriteria;
import com.team04.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "CommentController", description = "API 커멘트 컨트롤러")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * 이 API는 location, date, feelsLikeTemperature, month 파라미터를 사용하여 필터링된 커멘트 목록을 조회합니다.
     * 필터링 조건이 없으면 전체 커멘트를 조회합니다.
     * 여행자 추천 : location + date 조합 또는 location + feelsLikeTemperature 조합
     * 검색 필터 : location + feelsLikeTemperature + month 조합 (3! = 6가지 조합)
     * @param location 위치 필터링
     * @param date 날짜 필터링
     * @param feelsLikeTemperature 체감 온도 필터링
     * @param month 월 필터링
     * @param pageable 페이지 정보
     * @return 커멘트 DTO 목록
     */
    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "커멘트 다건 조회", description = "필터링된 커멘트 목록을 조회합니다.")
    public Page<CommentDto> getComments(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Double feelsLikeTemperature,
            @RequestParam(required = false) Integer month,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        CommentSearchCriteria criteria = CommentSearchCriteria.builder()
                .location(location)
                .date(date)
                .feelsLikeTemperature(feelsLikeTemperature)
                .month(month)
                .build();

        Page<Comment> items = commentService.findByCriteria(criteria, pageable);
        return items.map(CommentDto::new);
    }

    /**
     * ID로 커멘트를 조회합니다.
     * @param id 커멘트 ID
     * @return 커멘트 DTO
     */
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "커멘트 단건 조회", description = "ID로 커멘트를 조회합니다.")
    public CommentDto getComment(@PathVariable int id) {
        Comment comment = commentService.findById(id).get();
        return new CommentDto(comment);
    }


    record verifyPasswordReqBody(
            @NonNull String password
    ) {}

    @PostMapping("/{id}/verify-password")
    @Transactional(readOnly = true)
    @Operation(summary = "커멘트 비밀번호 검증", description = "커멘트의 비밀번호를 검증합니다.")
    public RsData<Boolean> verifyPassword(
            @PathVariable int id,
            @RequestBody @NonNull verifyPasswordReqBody passwordReqBody
    ) {
        Comment comment = commentService.findById(id).get();

        boolean isVerified = commentService.verifyPassword(comment, passwordReqBody.password());
        if (!isVerified) {
            return new RsData<>("400-1", "비밀번호가 일치하지 않습니다.", false);
        }

        return new RsData<>(
                "200-1",
                "비밀번호가 일치합니다.",
                true
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "커멘트 삭제", description = "커멘트를 삭제합니다.")
    public RsData<CommentDto> deleteComment(@PathVariable int id) {
        Comment comment = commentService.findById(id).get();

        commentService.delete(comment);

        return new RsData<>(
                "200-1",
                "%d번 커멘트가 삭제되었습니다.".formatted(id),
                new CommentDto(comment)
        );
    }
}
