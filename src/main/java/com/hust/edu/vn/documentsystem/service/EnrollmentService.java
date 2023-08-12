package com.hust.edu.vn.documentsystem.service;


import com.hust.edu.vn.documentsystem.entity.Subject;

import java.util.List;

public interface EnrollmentService {
    boolean addEnrollmentSubject(List<Long> subjects);

    List<Subject> getAllEnrollmentSubjects(Long userId);

    boolean deleteEnrollmentSubjects(List<Long> subjects);
}
