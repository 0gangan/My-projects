package lzg.example.formalSecurityTest.controller;

import lzg.example.formalSecurityTest.domain.BookCategory;
import lzg.example.formalSecurityTest.repo.BookCategoryRepository;
import lzg.example.formalSecurityTest.service.BookCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/bookCategory")
public class BookCategoryController {

    @Autowired
    private BookCategoryRepository categoryRepo;
    @Autowired
    private BookCategoryService bookCategoryService;
    @GetMapping("/tree") //获取全部分类及其子分类
    public List<BookCategory> getCategoryTree() {
        List<BookCategory> all = categoryRepo.findAll();

        // 找到所有根节点（parent 为 null）
        List<BookCategory> roots = all.stream()
                .filter(c -> c.getParent() == null)
                .collect(Collectors.toList());

        return roots;
    }

    @GetMapping("/{id}") //根据id获取其全部子分类
    public ResponseEntity<BookCategory> getById(@PathVariable Long id) {
        return categoryRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public BookCategory add(@RequestBody BookCategory category) {
        if (category.getParent() != null && category.getParent().getId() != null) {
            Optional<BookCategory> parentOpt = categoryRepo.findById(category.getParent().getId());
            parentOpt.ifPresent(category::setParent); // ✅ 正确设置父分类
        } else {
            category.setParent(null);
        }

        return categoryRepo.save(category);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody BookCategory newCategory) {
        Optional<BookCategory> optional = categoryRepo.findById(id);
        if (optional.isPresent()) {
            BookCategory category = optional.get();
            category.setName(newCategory.getName());

            if (newCategory.getParentId() != null) {
                BookCategory parent = categoryRepo.findById(newCategory.getParentId()).orElse(null);
                category.setParent(parent);
            } else {
                category.setParent(null);
            }

            categoryRepo.save(category);
            return ResponseEntity.ok(category);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        bookCategoryService.deleteCategoryWithChildren(id);
        return ResponseEntity.ok().build();
    }

}
