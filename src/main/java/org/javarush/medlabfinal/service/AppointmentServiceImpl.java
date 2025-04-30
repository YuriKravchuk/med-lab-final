package org.javarush.medlabfinal.service;

import org.javarush.medlabfinal.entity.Appointment;
import org.javarush.medlabfinal.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository repository;

    @Override
    public void save(Appointment appointment) {
        repository.save(appointment);
    }

    @Override
    public Page<Appointment> findAllPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return repository.findAll(pageable);
    }

    @Override
    public List<Appointment> getAll() {
        return repository.findAll();
    }

    @Override
    public List<LocalTime> getTakenTimesByDate(LocalDate date) {
        return repository.findByDate(date).stream()
                .map(Appointment::getTime)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
