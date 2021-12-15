package com.cos.book.service;

import com.cos.book.domain.Book;
import com.cos.book.domain.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 기능을 정의할 수 있고, 트랜잭션을 관리할 수 있음.
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book 저장하기(Book book){
        return null;
    }

    public Book 한건가져오기(Long id) {
        return null;
    }

    public List<Book> 모두가져오기() {
        return null;
    }

}
