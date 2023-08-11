package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.Institute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstituteRepository extends JpaRepository<Institute, Long> {
    boolean existsByInstitute(String institute);
}