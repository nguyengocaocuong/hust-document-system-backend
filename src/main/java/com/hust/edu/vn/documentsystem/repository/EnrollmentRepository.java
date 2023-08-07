package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    @Query("""
            SELECT e FROM Enrollment e
            WHERE e.subject.id = :subjectId AND e.user.email = :email
            """
    )
    Enrollment findBySubjectIdAndUserEmail(Long subjectId, String email);
}