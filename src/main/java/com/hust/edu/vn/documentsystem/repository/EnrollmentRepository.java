package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
}