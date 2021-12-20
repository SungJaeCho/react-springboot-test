package com.cos.book.service;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 기능을 정의할 수 있고, 트랜잭션을 관리할 수 있음.
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Transactional
    public Book 저장하기(Book book){
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true) // JPA 변경감지라는 내부 기능 활성화 되지 않아 내부연산 줄임, update시의 정합성유지
    public Book 한건가져오기(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("id를 확인해주세요!"));
    }

    public List<Book> 모두가져오기() {
        return bookRepository.findAll();
    }

    @Transactional
    public Book 수정하기(Long id, Book book) {
        return null;
    }

    public String 삭제하기(Long id) {
        return null;
    }

}
