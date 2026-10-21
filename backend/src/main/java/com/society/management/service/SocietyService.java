package com.society.management.service;

import com.society.management.entity.Society;
import com.society.management.exception.DuplicateSocietyException;
import com.society.management.exception.SocietyNotFoundException;
import com.society.management.repository.SocietyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SocietyService {

    private final SocietyRepository societyRepository;

    public SocietyService(SocietyRepository societyRepository) {
        this.societyRepository = societyRepository;
    }

    @Transactional
    public Society createSociety(Society society) {
        if (societyRepository.existsByRegistrationNumber(society.getRegistrationNumber())) {
            throw new DuplicateSocietyException(
                "Society with registration number '" + society.getRegistrationNumber() + "' already exists."
            );
        }
        society.setId(null);
        return societyRepository.save(society);
    }

    @Transactional(readOnly = true)
    public Society getSocietyById(Long id) {
        return societyRepository.findById(id)
            .orElseThrow(() -> new SocietyNotFoundException("Society not found with id: " + id));
    }
}
