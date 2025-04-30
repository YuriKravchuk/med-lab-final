package org.javarush.medlabfinal.controller;

import jakarta.validation.Valid;
import org.javarush.medlabfinal.entity.Appointment;
import org.javarush.medlabfinal.entity.MedicalAnalysis;
import org.javarush.medlabfinal.service.AppointmentService;
import org.javarush.medlabfinal.service.EmailService;
import org.javarush.medlabfinal.service.MedicalAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicalAnalysisService medicalAnalysisService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/form")
    public String showAppointmentForm(Model model) {
        model.addAttribute("appointment", new Appointment());
        model.addAttribute("services", medicalAnalysisService.findAll());
        return "appointment_form";
    }

    @PostMapping("/form")
    public String saveAppointment(@ModelAttribute @Valid Appointment appointment,
                                  BindingResult bindingResult,
//                                  @RequestParam(required = false) List<Long> selectedServices,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

//        if (selectedServices != null) {
//            List<MedicalAnalysis> selected = selectedServices.stream()
//                    .map(id -> medicalAnalysisService.findById(id).orElse(null))
//                    .filter(Objects::nonNull)
//                    .toList();
//            appointment.setServices(selected);
//
//            double total = selected.stream().mapToDouble(MedicalAnalysis::getPrice).sum();
//            double discounted = total * 0.9;
//            appointment.setPriceWithDiscount(discounted);
//        }


        if (bindingResult.hasErrors() || appointment.getServices() == null || appointment.getServices().isEmpty()) {
            if (appointment.getServices() == null || appointment.getServices().isEmpty()) {
                model.addAttribute("serviceError", "Потрібно вибрати хоча б одну послугу");
            }
            model.addAttribute("services", medicalAnalysisService.findAll());
            return "appointment_form";
        }

        if (appointment.getServices() != null && !appointment.getServices().isEmpty()) {
            double total = appointment.getServices().stream().mapToDouble(MedicalAnalysis::getPrice).sum();
            double discounted = total * 0.9;
            appointment.setPriceWithDiscount(discounted);
        } else {
            model.addAttribute("serviceError", "Потрібно вибрати хоча б одну послугу");
            model.addAttribute("services", medicalAnalysisService.findAll());
            return "appointment_form";
        }

        appointmentService.save(appointment);

        if (appointment.getEmail() != null && !appointment.getEmail().isBlank()) {
            emailService.sendConfirmation(appointment);
        }

        redirectAttributes.addFlashAttribute("success", "Ви успішно записалися!");
        return "redirect:/appointments/form";
    }

    @GetMapping("/taken-times")
    @ResponseBody
    public List<LocalTime> getTakenTimes(@RequestParam String date) {
        return appointmentService.getTakenTimesByDate(LocalDate.parse(date));
    }

    @GetMapping
    public String viewAppointments(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   Model model) {
        Page<Appointment> appointmentPage = appointmentService.findAllPaginated(page, size);
        model.addAttribute("appointmentPage", appointmentPage);
        return "appointments";
    }



    @GetMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteById(id);
        return "redirect:/appointments";
    }

}
