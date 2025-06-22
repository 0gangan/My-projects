package lzg.example.formalSecurityTest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lzg.example.formalSecurityTest.domain.SecurityUser;
import lzg.example.formalSecurityTest.domain.UserEntity;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserEntityService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.getUserByName(username);
        if (null == user) {
            throw new RuntimeException(String.format("not found [%s]", username));
        }
        return new SecurityUser(user);
    }
}