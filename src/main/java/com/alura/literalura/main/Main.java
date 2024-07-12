package com.alura.literalura.main;

import com.alura.literalura.model.Author;
import com.alura.literalura.model.Book;
import com.alura.literalura.model.record.DataAuthor;
import com.alura.literalura.model.record.DataBook;
import com.alura.literalura.model.record.DataSearch;
import com.alura.literalura.model.record.Language;
import com.alura.literalura.repository.AuthorRepository;
import com.alura.literalura.repository.BookRepository;
import com.alura.literalura.service.APIConsumer;
import com.alura.literalura.service.DataConverter;

import java.util.*;
import java.util.stream.Collectors;


public class Main {
    private Scanner scanner = new Scanner(System.in);
    private APIConsumer apiConsumer = new APIConsumer();
    private final String BASE_URL = "https://gutendex.com/books/?search=";
    private DataConverter dataConverter = new DataConverter();
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private List<Author> authors;
    private List<Book> books;

    public Main(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void showMenu() {
        int option = -1;
        while (option != 0) {
            System.out.println("*********************************\n");
            var menu = """
                    1 - Search books by title
                    2 - Show registered books
                    3 - Show registered authors
                    4 - Authors alive in a specific year
                    5 - Search books by language
                    6 - Top 10 most downloaded books
                    7 - Most and least downloaded book
                    
                    0 - Exit
                    
                    """;

            System.out.println(menu);
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid format, please enter a number available in the menu!");
                scanner.nextLine();
            }
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    searchBook();
                    break;
                case 2:
                    showBooks();
                    break;
                case 3:
                    showAuthors();
                    break;
                case 4:
                    authorsAliveByYear();
                    break;
                case 5:
                    searchBookByLanguage();
                    break;
                case 6:
                    top10MostDownloadedBooks();
                    break;
                case 7:
                    bookRanking();
                    break;
                case 0:
                    System.out.println("Exiting the application");
                    break;
                default:
                    System.out.println("Invalid option\n");
            }
        }
    }

    private DataSearch getSearchData() {
        System.out.println("Enter the book name: ");
        var bookName = scanner.nextLine();
        var json = apiConsumer.fetchData(BASE_URL + bookName.replace(" ", "%20"));
        DataSearch searchData = dataConverter.convertData(json, DataSearch.class);
        return searchData;
    }

    private void searchBook() {
        DataSearch searchData = getSearchData();
        if (searchData != null && !searchData.books().isEmpty()) {
            DataBook firstBook = searchData.books().get(0);

            Book book = new Book(firstBook);

            Optional<Book> existingBook = bookRepository.findByTitle(book.getTitle());
            if (existingBook.isPresent()) {
                System.out.println("\nThe book is already registered\n");
            } else {
                if (!firstBook.author().isEmpty()) {
                    DataAuthor authorData = firstBook.author().get(0);
                    Author author = new Author(authorData);
                    Optional<Author> optionalAuthor = authorRepository.findByName(author.getName());

                    if (optionalAuthor.isPresent()) {
                        Author existingAuthor = optionalAuthor.get();
                        book.setAuthor(existingAuthor);
                        bookRepository.save(book);
                    } else {
                        Author newAuthor = authorRepository.save(author);
                        book.setAuthor(newAuthor);
                        bookRepository.save(book);
                    }

                    Integer downloadCount = book.getDownloadNumber() != null ? book.getDownloadNumber() : 0;
                    System.out.println("********** Book **********");
                    System.out.println("Title: " + book.getTitle());
                    System.out.println("Author: " + author.getName());
                    System.out.println("Language: " + book.getLanguage());
                    System.out.println("Download count: " + downloadCount);
                    System.out.println("***************************\n");
                } else {
                    System.out.println("No author");
                }
            }
        } else {
            System.out.println("Book not found");
        }
    }

    private void showBooks() {
        books = bookRepository.findAll();
        books.forEach(System.out::println);
    }

    private void showAuthors() {
        authors = authorRepository.findAll();
        authors.forEach(System.out::println);
    }

    private void authorsAliveByYear() {
        System.out.println("Enter the year to find living author(s): ");
        var year = scanner.nextInt();
        authors = authorRepository.findLivingAuthorsByYear(year);
        authors.forEach(System.out::println);
    }

    private List<Book> searchBooksByLanguage(String language) {
        var lang = Language.fromString(language);
        System.out.println("Searching for language: " + lang);

        List<Book> booksByLanguage = bookRepository.findByLanguage(lang);
        return booksByLanguage;
    }

    private void searchBookByLanguage() {
        System.out.println("Select the language you want to search for: ");

        int option = -1;
        while (option != 0) {
            var options = """
                    1. en - English
                    2. es - Spanish
                    3. fr - French
                    4. pt - Portuguese
                    
                    0. Return to previous options
                    """;
            System.out.println(options);
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid format, please enter a number available in the menu");
                scanner.nextLine();
            }
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    List<Book> booksInEnglish = searchBooksByLanguage("[en]");
                    booksInEnglish.forEach(System.out::println);
                    break;
                case 2:
                    List<Book> booksInSpanish = searchBooksByLanguage("[es]");
                    booksInSpanish.forEach(System.out::println);
                    break;
                case 3:
                    List<Book> booksInFrench = searchBooksByLanguage("[fr]");
                    booksInFrench.forEach(System.out::println);
                    break;
                case 4:
                    List<Book> booksInPortuguese = searchBooksByLanguage("[pt]");
                    booksInPortuguese.forEach(System.out::println);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("No language selected");
            }
        }
    }

    private void top10MostDownloadedBooks() {
        List<Book> topBooks = bookRepository.top10MostDownloadedBooks();
        topBooks.forEach(System.out::println);
    }

    private void bookRanking() {
        books = bookRepository.findAll();
        IntSummaryStatistics stats = books.stream()
                .filter(b -> b.getDownloadNumber() > 0)
                .collect(Collectors.summarizingInt(Book::getDownloadNumber));

        Book mostDownloadedBook = books.stream()
                .filter(b -> b.getDownloadNumber()== stats.getMax())
                .findFirst()
                .orElse(null);

        Book leastDownloadedBook = books.stream()
                .filter(b -> b.getDownloadNumber() == stats.getMin())
                .findFirst()
                .orElse(null);
        System.out.println();
        System.out.println("Most downloaded book: " + mostDownloadedBook.getTitle());
        System.out.println("Number of downloads: " + stats.getMax());
        System.out.println();
        System.out.println("Least downloaded book: " + leastDownloadedBook.getTitle());
        System.out.println("Number of downloads: " + stats.getMin());
        System.out.println();


    }
}

