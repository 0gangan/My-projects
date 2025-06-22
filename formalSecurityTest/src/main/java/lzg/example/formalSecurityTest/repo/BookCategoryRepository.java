package lzg.example.formalSecurityTest.repo;

import lzg.example.formalSecurityTest.domain.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    List<BookCategory> findByParentIsNull(); // 保留此方法用于获取根分类

    // ✅ 修改这个方法名，改成和实体字段一致
    List<BookCategory> findByParent(BookCategory parent);
}
