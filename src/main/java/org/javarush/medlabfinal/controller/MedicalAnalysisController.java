package org.javarush.medlabfinal.controller;

import jakarta.validation.Valid;
import org.javarush.medlabfinal.entity.MedicalAnalysis;
import org.javarush.medlabfinal.service.MedicalAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/services")
public class MedicalAnalysisController {

    @Autowired
    private MedicalAnalysisService service;

    @GetMapping
    public String showAllServices(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<MedicalAnalysis> services;
        if (keyword != null && !keyword.isBlank()) {
            services = service.searchByName(keyword);
        } else {
            services = service.findAll();
        }
        model.addAttribute("services", services);
        return "services";
    }

    @GetMapping("/add")
    public String addServiceForm(Model model) {
        model.addAttribute("medicalAnalysis", new MedicalAnalysis());
        return "add_service";
    }

    @PostMapping("/add")
    public String addService(@Valid @ModelAttribute("medicalAnalysis") MedicalAnalysis analysis,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add_service";
        }
        service.save(analysis);
        return "redirect:/services";
    }

    @GetMapping("/edit/{id}")
    public String editServiceForm(@ PathVariable Long id, Model model) {
        MedicalAnalysis analysis = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("medicalAnalysis", analysis);
        return "edit_service";
    }

    @PostMapping("/edit/{id}")
    public String updateService(@PathVariable Long id,
                                @Valid @ModelAttribute("medicalAnalysis") MedicalAnalysis updated,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "edit_service";
        }

        MedicalAnalysis analysis = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID"));

        analysis.setName(updated.getName());
        analysis.setPrice(updated.getPrice());
        analysis.setExecutionTime(updated.getExecutionTime());

        service.save(analysis);
        return "redirect:/services";
    }

    @GetMapping("/delete/{id}")
    public String deleteService(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/services";
    }

    @GetMapping("/view")
    public String viewOnlyServices(@RequestParam(required = false) String keyword, Model model) {
        List<MedicalAnalysis> analysis;
        if (keyword != null && !keyword.isBlank()) {
            analysis = service.searchByName(keyword);
        } else {
            analysis = service.findAll();
        }
        model.addAttribute("analysis", analysis);
        model.addAttribute("keyword", keyword);
        return "services_view_only";
    }

}
