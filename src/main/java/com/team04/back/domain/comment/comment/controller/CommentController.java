package com.team04.back.domain.comment.comment.controller;

import com.team04.back.domain.comment.comment.dto.CommentDto;
import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.service.CommentService;
import com.team04.back.global.exception.ServiceException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
     * location + date 조합 또는 location + feelsLikeTemperature 조합으로 커멘트 목록을 조회합니다.
     * 위 조합이 아닌 경우, 전체 커멘트 목록을 조회합니다.
     * 커멘트는 페이징 처리되어 반환됩니다.
     * @param location 위치
     * @param date 날짜
     * @param feelsLikeTemperature 체감 온도
     * @param pageable 페이징 정보
     * @return 커멘트 목록
     */
    @GetMapping
    @Operation(summary = "커멘트 다건 조회", description = "location, date, feelsLikeTemperature 파라미터를 사용하여 커멘트 목록을 조회합니다.")
    public Page<CommentDto> getComments(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Double feelsLikeTemperature,
            @PageableDefault(size = 10, page = 0, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (location == null && (date != null || feelsLikeTemperature != null)) {
            throw new ServiceException("400-1", "location 파라미터 없이는 date 또는 feelsLikeTemperature 파라미터를 사용할 수 없습니다.");
        }

        Page<Comment> items;

        if (location != null && date != null) {
            items = commentService.findByLocationAndDate(location, date, pageable);
        } else if (location != null && feelsLikeTemperature != null) {
            items = commentService.findByLocationAndTemperature(location, feelsLikeTemperature, pageable);
        } else {
            items = commentService.findAll(pageable);
        }

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
}
