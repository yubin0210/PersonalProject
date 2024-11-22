package com.yubin.SpringBootTest.controller;

import com.yubin.SpringBootTest.model.Event;
import com.yubin.SpringBootTest.model.User;
import com.yubin.SpringBootTest.service.EventService;
import com.yubin.SpringBootTest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    // JSON API: 사용자의 모든 이벤트 조회
    @GetMapping("/api")
    @ResponseBody
    public List<Map<String, Object>> getEventsApi(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = Optional.ofNullable(userService.findByUsername(username))
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Event> events = eventService.getEventsByUser(user);
        return events.stream().map(event -> {
            Map<String, Object> map = new HashMap<>();
            map.put("title", event.getTitle());
            map.put("start", event.getStartDate());
            map.put("end", event.getEndDate());
            map.put("description", event.getDescription());
            return map;
        }).collect(Collectors.toList());
    }



    // HTML 뷰: 이벤트 목록을 표시할 페이지
    @GetMapping
    public String getEventsPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String username = userDetails.getUsername();
        User user = Optional.ofNullable(userService.findByUsername(username))
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Event> events = eventService.getEventsByUser(user);
        model.addAttribute("events", events);
        return "events";  // events.html 템플릿으로 이벤트 목록 전달
    }

    // 특정 이벤트 조회 (JSON)
    @GetMapping("/api/{eventId}")
    @ResponseBody
    public Optional<Event> getEvent(@PathVariable Long eventId) {
        return eventService.getEventById(eventId);
    }

    // 이벤트 생성 (JSON)
    @PostMapping("/api")
    @ResponseBody
    public Event createEvent(@RequestBody Event event, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = Optional.ofNullable(userService.findByUsername(username))
                .orElseThrow(() -> new RuntimeException("User not found"));
        event.setUser(user);
        return eventService.createEvent(event);
    }

    // 이벤트 수정 (JSON)
    @PutMapping("/api/{eventId}")
    @ResponseBody
    public Event updateEvent(@PathVariable Long eventId, @RequestBody Event event) {
        return eventService.updateEvent(eventId, event);
    }

    // 이벤트 삭제 (JSON)
    @DeleteMapping("/api/{eventId}")
    @ResponseBody
    public void deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
    }
}
