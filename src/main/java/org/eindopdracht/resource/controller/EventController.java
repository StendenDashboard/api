package org.eindopdracht.resource.controller;

import org.eindopdracht.resource.dto.EventDTO;
import org.eindopdracht.resource.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin()
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Returns a list of all events.
     *
     * @return response entity with list of all events
     */
    @GetMapping
    public ResponseEntity<List<EventDTO>> get() {
        return ResponseEntity.ok(eventService.get());
    }

    /**
     * Returns a single event.
     *
     * @param id id of the event to find
     * @return response entity with single event
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    /**
     * Post a single event.
     *
     * @param eventDto event to post
     * @return response entity with posted event
     */
    @PostMapping
    public ResponseEntity<EventDTO> post(@RequestBody @Valid EventDTO eventDto) {
        return ResponseEntity.ok(eventService.persist(eventDto));
    }

    /**
     * Put a single event.
     *
     * @param id       id of the event to put
     * @param eventDto event to put
     * @return response entity with put event
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> put(@PathVariable int id, @RequestBody @Valid EventDTO eventDto) {
        return ResponseEntity.ok(eventService.put(id, eventDto));
    }

    /**
     * Delete a single event.
     *
     * @param id id of the event to delete
     * @return response entity with deleted event
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<EventDTO> delete(@PathVariable int id) {
        return ResponseEntity.ok(eventService.delete(id));
    }
}
