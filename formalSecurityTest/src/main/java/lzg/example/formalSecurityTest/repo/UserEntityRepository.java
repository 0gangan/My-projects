package lzg.example.formalSecurityTest.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import lzg.example.formalSecurityTest.domain.UserEntity;


import jakarta.transaction.Transactional;

@Transactional
public interface UserEntityRepository extends CrudRepository<UserEntity, Long>{
    @Query(value = "select * from user_entity", nativeQuery = true)
    List<UserEntity> findAll();
    @Query(value = "select * from user_entity where username=?1", nativeQuery = true)
    UserEntity getUserByName(String username);
    @Modifying
    @Query(value = "update user_entity r set r.username=?1 where r.id=?2", nativeQuery = true)
    int modifyById(
            String username,
            int id
    );
//    @Query(value = "select * from user_entity where id = ?1", nativeQuery = true)
//    UserEntity findByIdLong(Long id);

}