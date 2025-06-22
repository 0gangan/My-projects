package lzg.example.formalSecurityTest.controller;

import lzg.example.formalSecurityTest.domain.Book;
import lzg.example.formalSecurityTest.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")  // 允许跨域，方便前端请求
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // 查询所有图书
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    // 添加图书
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.save(book);
    }

    // 修改图书
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.findById(id).map(existingBook -> {
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(book.getAuthor());
            existingBook.setCategory(book.getCategory());
            existingBook.setPublisher(book.getPublisher());
            Book updatedBook = bookService.save(existingBook);
            return ResponseEntity.ok(updatedBook);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 删除图书
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        return bookService.findById(id).map(book -> {
            bookService.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //搜索图书
    @GetMapping("/search")
    public List<Book> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String publisher) {

        // 调用 service 层实现模糊查询
        return bookService.searchBooks(title, author, category, publisher);
    }

}