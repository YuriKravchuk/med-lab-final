package org.javarush.medlabfinal.controller;

import jakarta.servlet.http.HttpSession;
import org.javarush.medlabfinal.entity.Admin;
import org.javarush.medlabfinal.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/admin/login")
    public String loginPage() {
        return "admin_login"; // сторінка введення пароля
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String password, HttpSession session) {
        Admin admin = adminRepository.findFirstByOrderByIdAsc(); // отримуємо першого адміністратора
        if (admin != null && admin.getPassword().equals(password)) {
            session.setAttribute("isAdmin", true);
            return "redirect:/admin/panel"; // адмін панель
        }
        return "redirect:/admin/login?error"; // якщо пароль неправильний
    }

    @GetMapping("/admin/panel")
    public String adminPanel(HttpSession session) {
        if (isAdminAuthenticated(session)) {
            return "admin_panel"; // сторінка адмін-панелі
        }
        return "redirect:/admin/login"; // якщо не увійшов, редирект на вхід
    }

    @PostMapping("/admin/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session, Model model) {

        if (!isAdminAuthenticated(session)) {
            return "redirect:/admin/login"; // перевірка, чи авторизовано
        }

        Admin admin = adminRepository.findFirstByOrderByIdAsc();
        if (admin == null) {
            model.addAttribute("error", "Адміністратор не знайдений.");
            return "admin_panel";
        }

        // Перевірка поточного пароля
        if (!admin.getPassword().equals(currentPassword)) {
            model.addAttribute("error", "Неправильний поточний пароль.");
            return "admin_panel";
        }

        // Перевірка, чи новий пароль і підтвердження збігаються
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Паролі не збігаються.");
            return "admin_panel";
        }

        // Зміна пароля
        admin.setPassword(newPassword);
        adminRepository.save(admin);
        model.addAttribute("success", "Пароль успішно змінено.");

        return "admin_panel";
    }

    private boolean isAdminAuthenticated(HttpSession session) {
        return session.getAttribute("isAdmin") != null;
    }
}
