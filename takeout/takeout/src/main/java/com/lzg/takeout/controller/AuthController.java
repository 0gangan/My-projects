package com.lzg.takeout.controller;

import com.lzg.takeout.dto.LoginRequest;
import com.lzg.takeout.dto.LoginResponse;
import com.lzg.takeout.entity.User;
import com.lzg.takeout.repository.UserRepository;
import com.lzg.takeout.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder encoder;

    /** 登录接口：验证成功返回 JWT */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authManager.authenticate(authToken); // 登录验证

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication.getName());

        // 获取当前登录用户的信息（从数据库）
        Optional<User> optionalUser = userRepo.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        User user = optionalUser.get();

        // 构建并返回包含用户资料的响应，新增id字段
        return ResponseEntity.ok(new LoginResponse(
                user.getId(),     // 新增ID字段
                jwt,
                user.getUsername(),
                user.getRole(),
                user.getEmail(),
                user.getPhone(),
                user.getAddress()
        ));
    }

    /** 简易注册接口：保存用户并返回 JWT */
    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole("BUYER");
        user.setEmail(""); // 可选：默认空，或从前端传入
        user.setPhone(""); // 可选：默认空，或从前端传入
        userRepo.save(user);

        // 注册成功后自动登录
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        Authentication authentication = authManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateToken(authentication.getName());

        // 从数据库再查一次，获取完整信息
        Optional<User> optionalUser = userRepo.findByUsername(request.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户注册后找不到");
        }

        User savedUser = optionalUser.get();

        return ResponseEntity.ok(new LoginResponse(
                savedUser.getId(),    // 新增id字段
                jwt,
                savedUser.getUsername(),
                savedUser.getRole(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getAddress()
        ));
    }


    @GetMapping("/user/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("缺少或非法的 Authorization 头");
        }

        String token = header.substring(7);
        if (!jwtUtils.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("无效的 Token");
        }

        String username = jwtUtils.getUsername(token);

        // 返回完整的用户对象（不包含 password）
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户不存在");
        }

        User user = optionalUser.get();
        user.setPassword(null); // 为安全起见，不返回密码
        return ResponseEntity.ok(user);
    }
    @PutMapping("/me")
    public ResponseEntity<?> updateMe(HttpServletRequest request, @RequestBody User updated) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未登录");
        }
        String username = jwtUtils.getUsername(token.substring(7));
        Optional<User> optionalUser = userRepo.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户不存在");
        }

        User user = optionalUser.get();
        user.setPhone(updated.getPhone());
        user.setEmail(updated.getEmail());
        userRepo.save(user);
        return ResponseEntity.ok("修改成功");
    }

}
