package com.team04.back.domain.comment.comment.controller;

import com.team04.back.domain.comment.comment.dto.CommentDto;
import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Double feelsLikeTemperature
    ) {
        List<Comment> items;

        if (location != null && date != null) {
            items = commentService.findByLocationAndDate(location, date);
        } else if (location != null && feelsLikeTemperature != null) {
            items = commentService.findByLocationAndTemperature(location, feelsLikeTemperature);
        } else {
            items = commentService.findAll();
        }

        return items.stream()
                .map(CommentDto::new)
                .toList();
    }
}
