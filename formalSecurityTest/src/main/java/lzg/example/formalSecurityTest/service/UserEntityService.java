package lzg.example.formalSecurityTest.service;

import java.util.List;

import lzg.example.formalSecurityTest.domain.UserEntity;

public interface UserEntityService {
    UserEntity getUserByName(String username);
    UserEntity saveUser(UserEntity userEntity);
    int updateUser(UserEntity userEntity);
    void deleteUser(UserEntity userEntity);
    List<UserEntity> getUsers();
}