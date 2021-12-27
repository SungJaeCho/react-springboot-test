package com.cos.book.service;

// 단위 테스트 (Service와 관련된 애들만 메모리에 띄우면 됨)

import com.cos.book.domain.BookRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 단위 테스트 (Service와 관련된 애들만 메모리에 띄우면됨.)
 * BookRepository => 가짜 객체로 만들수 있음.
 */

@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {

    @InjectMocks
    // bookService 객체가 만들어질때 BookServiceUnitTest 파일에 @Mock로 등록된 모든 애들을 주입받는다. -> 현재의 경우 이거실행되면 bookRepository도 주입됨
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
}
