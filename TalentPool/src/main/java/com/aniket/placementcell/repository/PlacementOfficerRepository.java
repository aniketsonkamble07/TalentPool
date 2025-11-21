package com.aniket.placementcell.repository;

import com.aniket.placementcell.entity.PlacementOfficer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlacementOfficerRepository extends JpaRepository<PlacementOfficer, Long> {
    Optional<PlacementOfficer> findByEmail(String email);
    boolean existsByEmail(String email);
    int countByIsActive(boolean status);
    long count();
    Page<PlacementOfficer> findAll(Pageable pageable);


}

