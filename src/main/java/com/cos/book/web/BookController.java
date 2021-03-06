package com.cos.book.web;

import com.cos.book.domain.Book;
import com.cos.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    // security (라이브러리 적용) = CORS 저책을 가지고 있음. (시큐리티가 CORS를 해제하게 개발해야함)
    @CrossOrigin // cors 정책 무시 외부에서 들어오는 자바 스크립트 요청 허용
    @PostMapping("/book")
    public ResponseEntity<?> save(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.저장하기(book), HttpStatus.CREATED); // 200
    }

    @CrossOrigin // cors 정책 무시 외부에서 들어오는 자바 스크립트 요청 허용
    @GetMapping("/book")
    public ResponseEntity<?> findAll() {
        return new ResponseEntity<>(bookService.모두가져오기(), HttpStatus.OK); // 200
    }

    @CrossOrigin // cors 정책 무시 외부에서 들어오는 자바 스크립트 요청 허용
    @GetMapping("/book/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        System.out.println(id);
        return new ResponseEntity<>(bookService.한건가져오기(id), HttpStatus.OK); // 200
    }

    @CrossOrigin // cors 정책 무시 외부에서 들어오는 자바 스크립트 요청 허용
    @PutMapping("/book/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Book book) {
        return new ResponseEntity<>(bookService.수정하기(id, book), HttpStatus.OK); // 200
    }

    @CrossOrigin // cors 정책 무시 외부에서 들어오는 자바 스크립트 요청 허용
    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        return new ResponseEntity<>(bookService.삭제하기(id), HttpStatus.OK); // 200
    }
}
