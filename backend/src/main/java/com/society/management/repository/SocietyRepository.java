package com.society.management.repository;

import com.society.management.entity.Society;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocietyRepository extends JpaRepository<Society, Long> {

    boolean existsByRegistrationNumber(String registrationNumber);

    Optional<Society> findByRegistrationNumber(String registrationNumber);
}
