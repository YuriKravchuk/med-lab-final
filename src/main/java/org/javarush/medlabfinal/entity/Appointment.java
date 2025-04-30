package org.javarush.medlabfinal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Прізвище обов'язкове")
    @Size(min = 2, max = 50, message = "Прізвище має містити від 2 до 50 символів")
    private String lastName;

    @NotBlank(message = "Ім'я обов'язкове")
    @Size(min = 2, max = 50, message = "Ім'я має містити від 2 до 50 символів")
    private String firstName;

    @NotBlank(message = "Телефон обов'язковий")
    @Pattern(regexp = "\\+?\\d{10,15}", message = "Невірний формат телефону")
    private String phone;

    @NotBlank(message = "Email обов'язковий")
    @Email(message = "Невірний формат email")
    private String email;

    @Column(scale = 2)
    private Double priceWithDiscount;

    @NotNull(message = "Дата обов'язкова")
    @FutureOrPresent(message = "Дата не може бути в минулому")
    private LocalDate date;

    @NotNull(message = "Час обов'язковий")
    private LocalTime time;

    @NotEmpty(message = "Потрібно вибрати щонайменше одну послугу")
    @ManyToMany
    @JoinTable(name = "appointment_analysis",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_analysis_id"))
    private List<MedicalAnalysis> services;
}
