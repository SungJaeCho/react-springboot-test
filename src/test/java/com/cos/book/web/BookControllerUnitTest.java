package com.cos.book.web;

// 단위 테스트 ( Controller 관련 로직만 띄우기 ) Filter, ControllerAdvice
// https://howtodoinjava.com/spring-boot2/testing/ 사이트에서 정보 확인가능

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;

@Slf4j
@WebMvcTest // 스프링 환경으로 테스트한다는 의미 꼭 넣어야함
public class BookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // IOC환경에 bean 등록됨. 단위테스트에서만 필요
    private BookService bookService;

    // BDDMockito 패턴
    @Test
    public void save_테스트() throws Exception {
        // given (테스트를 하기위한 준비)
        Book book = new Book(null, "스프링따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book);
        // stub(행동 미리 지정) 단위테스트에서만 필요
        when(bookService.저장하기(book)).thenReturn(new Book(1L, "스프링따라하기", "코스"));

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
        books.add(new Book(1L, "스프링부트 따라하기", "코스"));
        books.add(new Book(2L, "리액트 따라하기", "코스"));
        when(bookService.모두가져오기()).thenReturn(books);

        //when
        ResultActions resultAction = mockMvc.perform(get("/book").accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction.andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("스프링부트 따라하기"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void findById_테스트() throws Exception {
        // given
        Long id = 1L;
        when(bookService.한건가져오기(id)).thenReturn(new Book(1L, "자바 공부하기", "쌀"));

        //when
        ResultActions resultAction = mockMvc.perform(get("/book/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("자바 공부하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void update_테스트() throws Exception {
        // given
        Long id = 1L;
        Book book = new Book(null, "C++따라하기", "코스");
        String content = new ObjectMapper().writeValueAsString(book);
        when(bookService.수정하기(id,book)).thenReturn(new Book(1L, "C++따라하기", "코스"));

        // when (테스트 실행)
        ResultActions resultAction = mockMvc.perform(put("/book/{id}",id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content)
                .accept(MediaType.APPLICATION_JSON_UTF8));

        //then
        resultAction.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("C++따라하기"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_테스트() throws Exception {
        // given
        Long id = 1L;
        when(bookService.삭제하기(id)).thenReturn("ok");

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
