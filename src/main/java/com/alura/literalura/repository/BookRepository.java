package com.alura.literalura.repository;


import com.alura.literalura.model.Book;
import com.alura.literalura.model.record.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByLanguage(Language language);

    Optional<Book> findByTitle(String title);

    @Query("SELECT l FROM Book l ORDER BY l.downloadNumber DESC")
    List<Book> top10MostDownloadedBooks();
}

