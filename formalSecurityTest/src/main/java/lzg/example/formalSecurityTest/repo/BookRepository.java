package lzg.example.formalSecurityTest.repo;

import lzg.example.formalSecurityTest.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR b.title LIKE CONCAT('%', :title, '%')) AND " +
            "(:author IS NULL OR b.author LIKE CONCAT('%', :author, '%')) AND " +
            "(:category IS NULL OR b.category LIKE CONCAT('%', :category, '%')) AND " +
            "(:publisher IS NULL OR b.publisher LIKE CONCAT('%', :publisher, '%'))")
    List<Book> searchBooks(@Param("title") String title,
                           @Param("author") String author,
                           @Param("category") String category,
                           @Param("publisher") String publisher);

}