package org.javarush.medlabfinal.service;

import org.javarush.medlabfinal.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendConfirmation(Appointment appointment) {
        StringBuilder sb = new StringBuilder("Дякуємо за запис!\n");
        sb.append("Дата: ").append(appointment.getDate()).append("\n");
        sb.append("Час: ").append(appointment.getTime()).append("\n");
        sb.append("Послуги:\n");
        appointment.getServices().forEach(service -> sb.append("- ")
                .append(service.getName()).append(": ").append(service.getPrice()).append(" грн\n"));
        sb.append("Загальна сума зі знижкою 10%: ").append(appointment.getPriceWithDiscount()).append(" грн");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appointment.getEmail());
        message.setSubject("Підтвердження запису на аналізи");
        message.setText(sb.toString());

        mailSender.send(message);
    }
}
