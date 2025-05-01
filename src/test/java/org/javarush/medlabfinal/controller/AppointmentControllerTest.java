package org.javarush.medlabfinal.controller;

import jakarta.transaction.Transactional;
import org.javarush.medlabfinal.entity.Appointment;
import org.javarush.medlabfinal.entity.MedicalAnalysis;
import org.javarush.medlabfinal.repository.AppointmentRepository;
import org.javarush.medlabfinal.service.AppointmentService;
import org.javarush.medlabfinal.service.EmailService;
import org.javarush.medlabfinal.service.MedicalAnalysisService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicalAnalysisService medicalAnalysisService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private MedicalAnalysis testService1;
    private MedicalAnalysis testService2;

    @BeforeEach
    void setUp() {
        testService1 = new MedicalAnalysis("Test Service 1", 100.0, 24);
        testService2 = new MedicalAnalysis("Test Service 2", 150.0, 48);
        medicalAnalysisService.save(testService1);
        medicalAnalysisService.save(testService2);

        assertNotNull(testService1.getId(), "testService1 ID має бути згенеровано");
        assertNotNull(testService2.getId(), "testService2 ID має бути згенеровано");
    }

    @Test
    void testShowAppointmentFormSuccess() throws Exception {
        mockMvc.perform(get("/appointments/form"))
                .andExpect(status().isOk())
                .andExpect(view().name("appointment_form"))
                .andExpect(model().attributeExists("appointment"))
                .andExpect(model().attributeExists("services"));
    }

    @Test
    void testSaveAppointmentValid() throws Exception {
        long countBefore = appointmentRepository.count();

        mockMvc.perform(post("/appointments/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", "Test")
                        .param("firstName", "User")
                        .param("phone", "+380123456789")
                        .param("email", "testuser@example.com")
                        .param("date", LocalDate.now().plusDays(1).toString())
                        .param("time", LocalTime.of(10, 0).toString())
                        .param("services", testService1.getId().toString(), testService2.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointments/form"))
                .andExpect(flash().attributeExists("success"));

        long countAfter = appointmentRepository.count();
        assertEquals(countBefore + 1, countAfter, "Кількість записів має збільшитися на 1");
    }

    @Test
    void testSaveAppointmentInvalidIfLastNameIsNull() throws Exception {
        mockMvc.perform(post("/appointments/form")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("lastName", "")
                        .param("firstName", "User")
                        .param("phone", "+380123456789")
                        .param("email", "testuser@example.com")
                        .param("date", LocalDate.now().plusDays(1).toString())
                        .param("time", LocalTime.of(10, 0).toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("appointment_form"))
                .andExpect(model().attributeExists("serviceError"));
    }

    @Test
    void testDeleteAppointmentSuccess() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setLastName("Test");
        appointment.setFirstName("User");
        appointment.setPhone("+380123456789");
        appointment.setEmail("testuser@example.com");
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setTime(LocalTime.of(10, 0));
        appointment.setServices(List.of(testService1));
        appointmentService.save(appointment);

        Long id = appointment.getId();
        assertTrue(appointmentRepository.existsById(id), "Запис має існувати перед видаленням");

        mockMvc.perform(get("/appointments/delete/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/appointments"));

        assertFalse(appointmentRepository.existsById(id), "Запис має бути видалений після запиту");
    }
}