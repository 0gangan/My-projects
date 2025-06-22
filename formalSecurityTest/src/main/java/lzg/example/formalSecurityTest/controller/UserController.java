package lzg.example.formalSecurityTest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lzg.example.formalSecurityTest.domain.UserEntity;
import lzg.example.formalSecurityTest.service.UserEntityService;
import lzg.example.formalSecurityTest.utils.R;
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserEntityService userService;
    @PostMapping("/getusers")
    public R getUsers() {
        List<UserEntity> entitys = userService.getUsers();
        return R.success(entitys);
    }
    @PostMapping("/updateuser")
    public R updateUser(@RequestBody UserEntity userEntity) {
        int yn = userService.updateUser(userEntity);
        String str= "update success";
        return R.success(str);
    }
    @PostMapping("/deleteuser")
    public R deleteUser(@RequestBody UserEntity userEntity) {
        userService.deleteUser(userEntity);
        String str = "delete success";
        return R.success(str);
    }
}