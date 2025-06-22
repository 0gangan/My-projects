package lzg.example.formalSecurityTest.service;

import jakarta.persistence.criteria.Predicate;
import lzg.example.formalSecurityTest.domain.Borrow;
import lzg.example.formalSecurityTest.domain.UserEntity;
import lzg.example.formalSecurityTest.domain.Book;
import lzg.example.formalSecurityTest.repo.BorrowRepository;
import lzg.example.formalSecurityTest.repo.UserEntityRepository;
import lzg.example.formalSecurityTest.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowService {

    @Autowired
    private BorrowRepository borrowRepo;

    @Autowired
    private UserEntityRepository userRepo;

    @Autowired
    private BookRepository bookRepo;

    public Borrow saveBorrow(Borrow borrow) {
        Optional<UserEntity> userOpt = userRepo.findById(borrow.getUserId());
        if (userOpt.isPresent()) {
            borrow.setUsername(userOpt.get().getUsername());
        } else {
            throw new RuntimeException("用户不存在: " + borrow.getUserId());
        }

        Optional<Book> bookOpt = bookRepo.findById(borrow.getBookId());
        if (bookOpt.isPresent()) {
            borrow.setBookTitle(bookOpt.get().getTitle());
        } else {
            throw new RuntimeException("图书不存在: " + borrow.getBookId());
        }

        if (borrow.getBorrowTime() == null) {
            borrow.setBorrowTime(LocalDateTime.now());
        }

        return borrowRepo.save(borrow);
    }

    public List<Borrow> getAllBorrows() {
        return (List<Borrow>) borrowRepo.findAll();
    }

    public void deleteBorrow(Long id) {
        borrowRepo.deleteById(id);
    }

    public Borrow updateBorrow(Borrow borrow) {
        Optional<Borrow> optional = borrowRepo.findById(borrow.getId());
        if (optional.isPresent()) {
            Borrow existing = optional.get();
            // 更新你允许被修改的字段
            existing.setUserId(borrow.getUserId());
            existing.setBookId(borrow.getBookId());
            existing.setBorrowTime(borrow.getBorrowTime());
            existing.setReturnTime(borrow.getReturnTime());
            existing.setReturned(borrow.getReturned());

            // 更新 username 和 bookTitle
            userRepo.findById(borrow.getUserId()).ifPresent(user -> {
                existing.setUsername(user.getUsername());
            });

            bookRepo.findById(borrow.getBookId()).ifPresent(book -> {
                existing.setBookTitle(book.getTitle());
            });
            return borrowRepo.save(existing);
        } else {
            throw new RuntimeException("借阅记录不存在，id = " + borrow.getId());
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(BorrowService.class);

    public Page<Borrow> getBorrowPage(Pageable pageable) {
        return borrowRepo.findAll(pageable); // 这才返回 Page<Borrow>
    }
    public Page<Borrow> getBorrowsByUserIdFiltered(Long userId, String bookTitle, Boolean returned, Pageable pageable) {
        logger.info("getBorrowsByUserIdFiltered called with parameters: userId={}, bookTitle={}, returned={}, pageable={}",
                userId, bookTitle, returned, pageable);

        return borrowRepo.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("userId"), userId));

            if (bookTitle != null && !bookTitle.isEmpty()) {
                logger.info("Adding bookTitle filter: LIKE %{}%", bookTitle);
                predicates.add(cb.like(root.get("bookTitle").as(String.class), "%" + bookTitle + "%"));
            }
            if (returned != null) {
                logger.info("Adding returned filter: = {}", returned);
                predicates.add(cb.equal(root.get("returned"), returned));
            }

            Predicate finalPredicate = cb.and(predicates.toArray(new Predicate[0]));
            logger.info("Final predicate created: {}", finalPredicate);
            return finalPredicate;
        }, pageable);
    }

    public Page<Borrow> getBorrowsByUserId(Long userId, Pageable pageable) {
        return borrowRepo.findByUserId(userId, pageable);
    }

}
