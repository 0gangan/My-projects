package lzg.example.formalSecurityTest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lzg.example.formalSecurityTest.domain.UserEntity;
import lzg.example.formalSecurityTest.service.AuthService;
import lzg.example.formalSecurityTest.service.UserEntityService;
//import com.example.demo.service.AuthService;
import lzg.example.formalSecurityTest.utils.R;
import org.springframework.util.StringUtils;
@CrossOrigin    //跨域
@RestController  //RESTFUL API
@RequestMapping("/auth")  //不需要验证的路径
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserEntityService userService;
    @PostMapping("/login")
    public R login(@RequestBody UserEntity params) {
        String username = params.getUsername();
        String password = params.getPassword();
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return R.error(500, "用户名或密码为空！");
        }
        String token = authService.login(username, password);
        UserEntity user = userService.getUserByName(username);
        user.setRegionCode(token);
        return R.success(user);
    }

    @PostMapping("/add/user")
    public R addUser(@RequestBody UserEntity userEntity) {
        UserEntity entity = authService.addUser(userEntity);
        return R.success(entity);
    }
}