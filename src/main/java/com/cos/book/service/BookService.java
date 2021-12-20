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
        // 더티 체킹 update 치기
        Book bookEntity = bookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("id를 확인해주세요!")); // 영속화 (book 오브젝트)
        bookEntity.setTitle(book.getTitle());
        bookEntity.setAuthor(book.getAuthor());
        return bookEntity;
    } // 함수 종료 => 트랜잭션 종료 => 영속화 되어있는 데이터를 DB로 갱신(flush) => commit ===> 더티체킹

    @Transactional
    public String 삭제하기(Long id) {
        bookRepository.deleteById(id); //오류가 터지면 익셉션을 타니까 신경안써도됨
        return "ok";
    }

}
