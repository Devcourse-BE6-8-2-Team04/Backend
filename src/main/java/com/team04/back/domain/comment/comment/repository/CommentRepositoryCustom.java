package com.team04.back.domain.comment.comment.repository;

import com.team04.back.domain.comment.comment.dto.CommentSearchDto;
import com.team04.back.domain.comment.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<Comment> findBySearch(CommentSearchDto searchDto, Pageable pageable);
}
