package com.cos.book.web;

import com.cos.book.domain.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    통합테스트 ( 모든 Bean들을 똑같이 Ioc에 올리고 테스트 하는 것)
    WebEnvironment.MOCK  실제톰캣이 아닌 다른 톰캣으로 테스트
    WebEnvironment.RANDOM_PORT 실제 톰캣으로 테스트
    @AutoConfigureMockMvc MockMvc를 IOC에 등록해줌
    @Transactional 각각의 테스트함수가 종료될 때마다 트랜잭션을 rollback을 해주는 어노테이션
*/

@Slf4j
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BookControllerIntegreTest {

    @Autowired
    private MockMvc mockMvc;

    // BDDMockito 패턴
    @Test
    public void save_테스트() throws Exception {
        // given (테스트를 하기위한 준비)
        Book book = new Book(null, "스프링따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book);
        // stub(행동 미리 지정), 통합테스트에서는 필요없음 실제로 작동하기 때문

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(post("/book")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        // then (검증)
        resultAction.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("스프링따라하기")) // JsonPath문법에 따라 처리
                .andDo(MockMvcResultHandlers.print());
    }
}
