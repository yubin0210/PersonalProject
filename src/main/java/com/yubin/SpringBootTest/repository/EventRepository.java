package com.yubin.SpringBootTest.repository;

import com.yubin.SpringBootTest.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByUserId(Long userId);  // 사용자의 이벤트를 찾는 메서드
}
