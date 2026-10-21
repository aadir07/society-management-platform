package com.society.management.controller;

import com.society.management.dto.CreateSocietyRequest;
import com.society.management.dto.SocietyResponse;
import com.society.management.entity.Society;
import com.society.management.service.SocietyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/societies")
public class SocietyController {

    private final SocietyService societyService;

    public SocietyController(SocietyService societyService) {
        this.societyService = societyService;
    }

    @PostMapping
    public ResponseEntity<SocietyResponse> createSociety(@Valid @RequestBody CreateSocietyRequest request) {
        Society societyToCreate = request.toEntity();
        Society createdSociety = societyService.createSociety(societyToCreate);
        SocietyResponse response = SocietyResponse.fromEntity(createdSociety);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocietyResponse> getSocietyById(@PathVariable Long id) {
        Society society = societyService.getSocietyById(id);
        SocietyResponse response = SocietyResponse.fromEntity(society);
        return ResponseEntity.ok(response);
    }
}
