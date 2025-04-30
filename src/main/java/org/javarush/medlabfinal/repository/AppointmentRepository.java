package org.javarush.medlabfinal.repository;

import org.javarush.medlabfinal.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDate(LocalDate date);
}
