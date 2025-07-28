package com.team04.back.global.initData;

import com.team04.back.domain.comment.comment.entity.Comment;
import com.team04.back.domain.comment.comment.service.CommentService;
import com.team04.back.domain.weather.weather.entity.WeatherInfo;
import com.team04.back.domain.weather.weather.enums.Weather;
import com.team04.back.domain.weather.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {
    @Autowired
    @Lazy
    private BaseInitData self;

    private final CommentService commentService;
    private final WeatherService weatherService;

    @Bean
    ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        if (commentService.count() > 0) return;

        WeatherInfo weatherInfo1 = new WeatherInfo(Weather.CLEAR_SKY, 7.0, 33.0, 37.0, 24.0, "서울", LocalDate.parse("2022-07-28"));
        WeatherInfo weatherInfo2 = new WeatherInfo(Weather.SNOW, 8.0, -6.0, -2.0, -10.0, "일본 삿포로", LocalDate.parse("2023-01-15"));
        WeatherInfo weatherInfo3 = new WeatherInfo(Weather.FEW_CLOUDS, 12.0, 26.0, 29.0, 17.0, "프랑스 파리", LocalDate.parse("2023-08-05"));
        WeatherInfo weatherInfo4 = new WeatherInfo(Weather.MODERATE_RAIN, 7.0, 14.0, 16.0, 9.0, "영국 런던", LocalDate.parse("2023-10-20"));
        weatherService.save(weatherInfo1);
        weatherService.save(weatherInfo2);
        weatherService.save(weatherInfo3);
        weatherService.save(weatherInfo4);

        Comment comment1 = new Comment("user1@test.com", "1234", "https://images.unsplash.com/photo-1658874761235-8d56cbd5da2d?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8b290ZCUyMCVFQyU5NyVBQyVFQiVBNiU4NHxlbnwwfHwwfHx8MA%3D%3D", "한국 여름 엄청 더워요...", "#한국여름#폭염주의", weatherInfo1);
        Comment comment2 = new Comment("user2@test.com", "1234", "https://images.unsplash.com/photo-1638385583463-e3d424c22916?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MjB8fCVFQyU5RCVCQyVFQiVCMyVCOCUyMCVFQyU4MiVCRiVFRCU4RiVBQyVFQiVBMSU5QyUyMCVFQyVCRCU5NCVFQiU5NCU5NHxlbnwwfHwwfHx8MA%3D%3D", "삿포로는 겨울이 정말 추워요. 눈도 엄청 많이 와요!", "#일본#삿포로#겨울#눈폭탄", weatherInfo2);
        Comment comment3 = new Comment("user3@test.com", "1234", "https://images.unsplash.com/photo-1569789496053-095f2d6e3b05?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8OTR8fCVFRCU4QyU4QyVFQiVBNiVBQyUyMCVFQyU4MiVCMCVFQyVCMSU4NXxlbnwwfHwwfHx8MA%3D%3D", "파리는 여름에도 밤에는 시원하네요~ 낮엔 살짝 덥지만 걷기 좋아요!!", "#파리#유럽여행#여름날씨#산책", weatherInfo3);
        Comment comment4 = new Comment("user4@test.com", "1234", "https://images.unsplash.com/photo-1518090753814-263ac71fc863?w=500&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8M3x8JUVCJTlGJUIwJUVCJThEJTk4JTIwJUVDJTlBJUIwJUVDJTgyJUIwfGVufDB8fDB8fHww", "런던은 정말 자주 비가 오네요... 우산은 필수입니다 ☔", "#런던#가을비#우산#영국날씨", weatherInfo4);
        commentService.save(comment1);
        commentService.save(comment2);
        commentService.save(comment3);
        commentService.save(comment4);

        System.out.println("초기 데이터가 생성되었습니다.");
    }
}