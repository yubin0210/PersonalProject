package com.yubin.SpringBootTest.service;

import com.yubin.SpringBootTest.model.Event;
import com.yubin.SpringBootTest.repository.EventRepository;
import com.yubin.SpringBootTest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getEventsByUser(User user) {
        return eventRepository.findByUserId(user.getId());
    }

    public Optional<Event> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    public Event updateEvent(Long eventId, Event updatedEvent) {
        if (eventRepository.existsById(eventId)) {
            updatedEvent.setId(eventId); // 기존 ID로 수정
            return eventRepository.save(updatedEvent);
        }
        return null;
    }

    public void deleteEvent(Long eventId) {
        eventRepository.deleteById(eventId);
    }
}
