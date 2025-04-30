package org.javarush.medlabfinal.service;

import org.javarush.medlabfinal.entity.MedicalAnalysis;
import org.javarush.medlabfinal.repository.MedicalAnalysisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalAnalysisServiceImpl implements MedicalAnalysisService {
    @Autowired
    private MedicalAnalysisRepository repository;

    @Override
    public List<MedicalAnalysis> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(MedicalAnalysis analysis) {
        repository.save(analysis);
    }

    @Override
    public Optional<MedicalAnalysis> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<MedicalAnalysis> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

}
