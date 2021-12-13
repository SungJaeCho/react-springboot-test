package com.cos.book.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository를 적어야 스프링 IoC에 빈으로 등록되는데
// JpaRepository를 상속받으면 생략가능함
// JpaRepository는 CRUD 함수를 들고있음.
public interface BookRepository extends JpaRepository<Book,Long> {
}
