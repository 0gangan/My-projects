package lzg.example.formalSecurityTest.service;

import lzg.example.formalSecurityTest.domain.UserEntity;

public interface AuthService {
    String login(String username, String password);
    UserEntity addUser(UserEntity userEntity);
}
