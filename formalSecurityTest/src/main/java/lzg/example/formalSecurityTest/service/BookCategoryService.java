package lzg.example.formalSecurityTest.service;

import lzg.example.formalSecurityTest.domain.BookCategory;
import lzg.example.formalSecurityTest.repo.BookCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookCategoryService {

    @Autowired
    private BookCategoryRepository bookCategoryRepository;

    /**
     * 递归找出某分类及其所有子分类的ID列表
     */
    private void collectCategoryAndChildren(Long categoryId, List<Long> collector) {
        collector.add(categoryId);

        // ✅ 先获取父分类实体
        Optional<BookCategory> parentOpt = bookCategoryRepository.findById(categoryId);
        if (parentOpt.isPresent()) {
            BookCategory parent = parentOpt.get();
            List<BookCategory> children = bookCategoryRepository.findByParent(parent);

            for (BookCategory child : children) {
                collectCategoryAndChildren(child.getId(), collector); // 递归
            }
        }
    }

    /**
     * 删除某个分类及其所有子分类
     */
    public void deleteCategoryWithChildren(Long categoryId) {
        List<Long> toDeleteIds = new ArrayList<>();
        collectCategoryAndChildren(categoryId, toDeleteIds);
        // 批量删除
        bookCategoryRepository.deleteAllById(toDeleteIds);
    }
}
