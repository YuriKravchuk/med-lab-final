package org.javarush.medlabfinal.repository;

import org.javarush.medlabfinal.entity.MedicalAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalAnalysisRepository extends JpaRepository<MedicalAnalysis, Long> {
    List<MedicalAnalysis> findByNameContainingIgnoreCase(String name);
}