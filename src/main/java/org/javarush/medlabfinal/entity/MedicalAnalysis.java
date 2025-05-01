package org.javarush.medlabfinal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "medical_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Назва обов'язкова")
    @Size(min = 2, max = 100, message = "Назва повинна містити від 2 до 100 символів")
    private String name;

    @NotNull(message = "Ціна обов'язкова")
    @DecimalMin(value = "0.00", inclusive = false, message = "Ціна повинна бути більшою за 0")
    @Column(scale = 2)
    private Double price;

    public MedicalAnalysis(String name, Double price, Integer executionTime) {
        this.name = name;
        this.price = price;
        this.executionTime = executionTime;
    }

    @NotNull(message = "Термін виконання обов'язковий")
    @Min(value = 1, message = "Термін виконання повинен бути більшим за 0")
    private Integer executionTime;
}
