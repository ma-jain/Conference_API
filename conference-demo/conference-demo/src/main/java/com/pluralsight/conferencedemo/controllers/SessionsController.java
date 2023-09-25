package com.pluralsight.conferencedemo.controllers;

import com.pluralsight.conferencedemo.models.Session;
import com.pluralsight.conferencedemo.repositories.SessionRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
public class SessionsController {
    @Autowired
    private SessionRepository sessionRepository;

    @GetMapping
    public List<Session> list() {
        return sessionRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Session get(@PathVariable Long id) {
        return sessionRepository.getReferenceById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Session create(@RequestBody final Session session) {
        return sessionRepository.saveAndFlush(session);
        //flush means that it will update the data in the spring data jpa for us
        // only saving ot does not do the job.
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void Delete(@PathVariable Long id) {
        //does not delete the children data in jpa
         sessionRepository.deleteById(id);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    //PUT removes all the attributes from the entity we are updating
    //we can also use PATCH that will allow us to remove only a portion of the entities and update it
    public ResponseEntity<Session> update(@PathVariable Long id, @RequestBody Session session){
        if (session == null) {
            return ResponseEntity.badRequest().body(null); // Invalid request, session data is missing
        }
        Session existingSession = sessionRepository.getReferenceById(id);
        BeanUtils.copyProperties(session, existingSession, "session_id");
        return ResponseEntity.ok(sessionRepository.saveAndFlush(existingSession));
    }
}
