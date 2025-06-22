package lzg.example.formalSecurityTest.repo;

import lzg.example.formalSecurityTest.domain.Borrow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long>, JpaSpecificationExecutor<Borrow> {

    Page<Borrow> findByUserId(Long userId, Pageable pageable);

}
