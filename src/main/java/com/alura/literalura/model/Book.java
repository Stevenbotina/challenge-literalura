package com.alura.literalura.model;

import com.alura.literalura.model.record.DataBook;
import com.alura.literalura.model.record.Language;
import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @ManyToOne
    private Author author;
    @Enumerated(EnumType.STRING)
    private Language language;
    private Integer downloadNumber;

    public Book() {
    }

    public Book(DataBook dataBook) {
        this.title = dataBook.title();
        this.language = Language.fromString(dataBook.language().toString().split(",")[0].trim());
        this.downloadNumber = dataBook.downloadNumber();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Integer getDownloadNumber() {
        return downloadNumber;
    }

    public void setDownloadNumber(Integer downloadNumber) {
        this.downloadNumber = downloadNumber;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author=" + author +
                ", language=" + language +
                ", downloadNumber=" + downloadNumber +
                '}';
    }

}
