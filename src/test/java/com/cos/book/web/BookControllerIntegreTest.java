package com.cos.book.web;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager; // Jpa의 내부 매니저

    @BeforeEach //모든테스트함수가 각각 실행됨 안그러면 오토인크리먼트같은 경우 초기화가 되지 않음.
    public void init() {
//        entityManager.createNativeQuery("ALTER TABLE book AlTER COLUMN id RESTART WITH 1").executeUpdate(); //h2 db기준
        entityManager.createNativeQuery("ALTER TABLE book AUTO_INCREMENT = 1").executeUpdate(); //mysql db기준
    }

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


    @Test
    public void findAll_테스트() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(null,"스프링부트 따라하기","코스"));
        books.add(new Book(null,"리액트 따라하기","코스"));
        books.add(new Book(null,"아무거나 따라하기","코스"));
        bookRepository.saveAll(books);

        //when
        ResultActions resultAction = mockMvc.perform(get("/book").accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction.andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void findById_테스트() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(null,"스프링부트 따라하기","코스"));
        books.add(new Book(null,"리액트 따라하기","코스"));
        books.add(new Book(null,"아무거나 따라하기","코스"));
        bookRepository.saveAll(books);

        Long id = 1L;

        //when
        ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_테스트() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(null,"스프링부트 따라하기","코스"));
        books.add(new Book(null,"리액트 따라하기","코스"));
        books.add(new Book(null,"아무거나 따라하기","코스"));
        bookRepository.saveAll(books);

        Long id = 3L;
        //업데이트할 데이터
        Book book = new Book(null, "C++ 따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book);

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.title").value("C++ 따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void delete_테스트() throws Exception {
        // given
        List<Book> books = new ArrayList<>();
        books.add(new Book(null,"스프링부트 따라하기","코스"));
        books.add(new Book(null,"리액트 따라하기","코스"));
        books.add(new Book(null,"아무거나 따라하기","코스"));
        bookRepository.saveAll(books);

        Long id = 1L;

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(delete("/book/{id}",id)
                .accept(MediaType.TEXT_PLAIN));

        //then
        resultAction.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        // 문자응답시
        MvcResult requestResult = resultAction.andReturn();
        String result = requestResult.getResponse().getContentAsString();

        assertEquals("ok",result);
    }
}
