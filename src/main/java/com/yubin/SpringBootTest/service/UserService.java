package com.yubin.SpringBootTest.service;

import com.yubin.SpringBootTest.model.User;
import com.yubin.SpringBootTest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(User user) {
        System.out.println("저장할 사용자: " + user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // 비밀번호 인코딩
        User savedUser = userRepository.save(user);  // DB 저장
        System.out.println("저장된 사용자 ID: " + savedUser.getId());
        return savedUser;
    }


    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // 현재 로그인한 사용자의 ID를 가져오는 메서드
    public Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // 로그인한 사용자의 username을 가져옴
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return user.getId();  // 사용자 ID 반환
    }

    // userId로 User 조회
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}