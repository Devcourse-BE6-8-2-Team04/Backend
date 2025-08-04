package com.team04.back.domain.comment.comment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team04.back.domain.comment.comment.dto.CommentSearchDto;
import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.entity.QComment;
import com.team04.back.domain.weather.weather.entity.QWeatherInfo;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Comment> findBySearch(CommentSearchDto search, Pageable pageable) {
        QComment comment = QComment.comment;
        QWeatherInfo weather = QWeatherInfo.weatherInfo;

        BooleanBuilder builder = new BooleanBuilder();

        if (search.hasLocation()) {
            builder.and(weather.location.containsIgnoreCase(search.location()));
        }
        if (search.hasMonth()) {
            builder.and(weather.date.month().eq(search.month()));
        }
        if (search.hasFeelsLikeTemperature()) {
            double min = search.feelsLikeTemperature() - 2.5;
            double max = search.feelsLikeTemperature() + 2.5;
            builder.and(weather.feelsLikeTemperature.between(min, max));
        }
        if (search.hasDate()) {
            builder.and(weather.date.month().eq(search.date().getMonthValue()));
        }
        if (search.hasEmail()) {
            builder.and(comment.email.eq(search.email()));
        }

        // 데이터 조회
        List<Comment> content = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.weatherInfo, weather).fetchJoin()
                .where(builder) // BooleanBuilder 사용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(getOrderSpecifier(pageable, comment)) // 동적 정렬 적용
                .fetch();

        // 카운트 쿼리
        long total = queryFactory
                .selectFrom(comment)
                .leftJoin(comment.weatherInfo, weather)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 동적 정렬 처리
     */
    private OrderSpecifier<?>[] getOrderSpecifier(Pageable pageable, QComment comment) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        if (pageable.getSort().isEmpty()) {
            orders.add(comment.id.desc());
        } else {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                String property = order.getProperty();

                switch (property) {
                    case "id":
                        orders.add(new OrderSpecifier<>(direction, comment.id));
                        break;
                    case "email":
                        orders.add(new OrderSpecifier<>(direction, comment.email));
                        break;
                    case "location":
                        orders.add(new OrderSpecifier<>(direction, comment.weatherInfo.location));
                        break;
                    default:
                        orders.add(comment.id.desc());
                        break;
                }
            }
        }

        return orders.toArray(new OrderSpecifier[0]);
    }
}