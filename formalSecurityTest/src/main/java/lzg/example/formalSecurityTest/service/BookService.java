package lzg.example.formalSecurityTest.service;

import lzg.example.formalSecurityTest.domain.Book;
import lzg.example.formalSecurityTest.repo.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
    public List<Book> searchBooks(String title, String author, String category, String publisher) {
        return bookRepository.searchBooks(title, author, category, publisher);
    }
}
