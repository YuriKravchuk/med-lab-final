package org.javarush.medlabfinal.controller;

import jakarta.transaction.Transactional;
import org.javarush.medlabfinal.entity.MedicalAnalysis;
import org.javarush.medlabfinal.repository.AppointmentRepository;
import org.javarush.medlabfinal.repository.MedicalAnalysisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MedicalAnalysisControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicalAnalysisRepository repository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @AfterEach
    void tearDown() {
        appointmentRepository.deleteAll(); // очищаємо зв’язки
        repository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    void testShowAllServicesSuccess() throws Exception {
        MedicalAnalysis analysis = new MedicalAnalysis(null, "Blood Test", 200.0, 2);
        repository.save(analysis);

        mockMvc.perform(get("/services"))
                .andExpect(status().isOk())
                .andExpect(view().name("services"))
                .andExpect(model().attribute("services", hasSize(1)))
                .andExpect(model().attribute("services", hasItem(
                        allOf(
                                hasProperty("name", is("Blood Test")),
                                hasProperty("price", is(200.0)),
                                hasProperty("executionTime", is(2))
                        )
                )));
    }

    @Test
    void testAddServiceSuccess() throws Exception {
        mockMvc.perform(post("/services/add")
                        .param("name", "Urine Test")
                        .param("price", "150.00")
                        .param("executionTime", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));

        assertEquals(1, repository.count());
        assertTrue(repository.findByNameContainingIgnoreCase("Urine Test").size() > 0);
    }

    @Test
    void testDeleteServiceSuccess() throws Exception {
        MedicalAnalysis analysis = new MedicalAnalysis(null, "To Delete", 100.0, 1);
        repository.save(analysis);

        mockMvc.perform(get("/services/delete/{id}", analysis.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));

        Optional<MedicalAnalysis> deleted = repository.findById(analysis.getId());
        assertTrue(deleted.isEmpty());
    }

    @Test
    void testEditServiceSuccess() throws Exception {
        MedicalAnalysis original = new MedicalAnalysis(null, "Old Name", 120.0, 1);
        repository.save(original);

        mockMvc.perform(post("/services/edit/{id}", original.getId())
                        .param("name", "Updated Name")
                        .param("price", "180.00")
                        .param("executionTime", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));

        MedicalAnalysis updated = repository.findById(original.getId()).orElseThrow();
        assertEquals("Updated Name", updated.getName());
        assertEquals(180.0, updated.getPrice());
        assertEquals(2, updated.getExecutionTime());
    }

    @Test
    void testSearchByKeywordSuccess() throws Exception {
        repository.save(new MedicalAnalysis(null, "Glucose", 90.0, 1));
        repository.save(new MedicalAnalysis(null, "Cholesterol", 110.0, 2));

        mockMvc.perform(get("/services").param("keyword", "gluc"))
                .andExpect(status().isOk())
                .andExpect(view().name("services"))
                .andExpect(model().attribute("services", hasSize(1)))
                .andExpect(model().attribute("services", hasItem(
                        hasProperty("name", containsStringIgnoringCase("glucose"))
                )));
    }

}