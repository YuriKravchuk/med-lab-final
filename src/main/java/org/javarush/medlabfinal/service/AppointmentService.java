package org.javarush.medlabfinal.service;

import org.javarush.medlabfinal.entity.Appointment;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentService {
    void save(Appointment appointment);
    List<Appointment> getAll();
    List<LocalTime> getTakenTimesByDate(LocalDate date);
    Page<Appointment> findAllPaginated(int page, int size);
    void deleteById(Long id);
}
