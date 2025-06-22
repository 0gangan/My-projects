package lzg.example.formalSecurityTest.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
public class SecurityUser implements UserDetails{
    /**
     *
     */
    private static final long serialVersionUID = 2618570141030619877L;
    private UserEntity userEntity;

    public SecurityUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
// false 用户帐号已过期
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
// false 用户帐号已被锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
// false 用户凭证已过期
        return true;
    }

    @Override
    public boolean isEnabled() {
// false 用户已失效
        return true;
    }
}