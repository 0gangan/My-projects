package lzg.example.formalSecurityTest.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import lzg.example.formalSecurityTest.domain.Borrow;
import lzg.example.formalSecurityTest.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @GetMapping
    public Page<Borrow> getBorrowsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return borrowService.getBorrowPage(pageable);
    }

    @PostMapping
    public Borrow add(@RequestBody Borrow borrow) {
        return borrowService.saveBorrow(borrow);
    }

    @PutMapping("/{id}")
    public Borrow updateBorrow(@PathVariable Long id, @RequestBody Borrow borrow) {
        // 设置 ID，确保 URL 中的 ID 和请求体中一致
        borrow.setId(id);
        return borrowService.updateBorrow(borrow);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        borrowService.deleteBorrow(id);
    }

    @GetMapping("/user/{userId}")
    public Page<Borrow> getBorrowsByUserId(
            @PathVariable Long userId,
            @RequestParam(required = false) String bookTitle,
            @RequestParam(required = false) Boolean returned,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return borrowService.getBorrowsByUserIdFiltered(userId, bookTitle, returned, pageable);
    }


}
