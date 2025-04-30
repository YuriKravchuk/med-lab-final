package org.javarush.medlabfinal.repository;

import org.javarush.medlabfinal.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findFirstByOrderByIdAsc();
}
