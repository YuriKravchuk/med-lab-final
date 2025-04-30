package org.javarush.medlabfinal.service;

import org.javarush.medlabfinal.entity.MedicalAnalysis;

import java.util.List;
import java.util.Optional;

public interface MedicalAnalysisService {
    List<MedicalAnalysis> findAll();
    void save(MedicalAnalysis analysis);
    Optional<MedicalAnalysis> findById(Long id);
    void deleteById(Long id);
    List<MedicalAnalysis> searchByName(String name);
}
