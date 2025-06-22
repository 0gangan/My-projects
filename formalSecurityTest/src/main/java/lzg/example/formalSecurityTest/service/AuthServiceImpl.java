package lzg.example.formalSecurityTest.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import lzg.example.formalSecurityTest.constant.RedisKey;
import lzg.example.formalSecurityTest.domain.UserEntity;
import lzg.example.formalSecurityTest.repo.UserEntityRepository;
import lzg.example.formalSecurityTest.utils.JwtUtil;
import lzg.example.formalSecurityTest.domain.SecurityUser;
@Service
public class AuthServiceImpl implements AuthService {
    Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserEntityService userEntityService;

    @Override
    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (authenticate == null) {
            log.error("{username: {}, password: {}} 认证失败！", username, password);
            return null;
        }
        SecurityUser user = (SecurityUser) authenticate.getPrincipal();
// userEntity
        UserEntity userEntity = user.getUserEntity();
// 以用户表的行政区划代码作为盐值（这里主要是为了程序更好扩展实际开发中盐值可以是一些特殊或唯一的标识）
        String token = JwtUtil.createToken(userEntity.getRegionCode(), username, null);
        redisTemplate.opsForValue().set(String.format(RedisKey.AUTH_TOKEN_KEY, username),
                JSON.toJSONString(user), JwtUtil.JWT_EXPIRE_TIME, TimeUnit.MILLISECONDS);
        return token;
    }

    @Override
    public UserEntity addUser(UserEntity userEntity) {
        return userEntityService.saveUser(userEntity);
    }
}