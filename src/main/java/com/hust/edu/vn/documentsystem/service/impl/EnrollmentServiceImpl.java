package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.entity.Enrollment;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.EnrollmentRepository;
import com.hust.edu.vn.documentsystem.repository.SubjectRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.EnrollmentService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {
    private final SubjectRepository subjectRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public EnrollmentServiceImpl(SubjectRepository subjectRepository, EnrollmentRepository enrollmentRepository, UserRepository userRepository) {
        this.subjectRepository = subjectRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean addEnrollmentSubject(List<Long> subjects) {
        List<Subject> listSubjects = subjectRepository.findAllByListId(subjects);
        if(listSubjects.size() == 0) return false;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Enrollment> listEnrollments = new ArrayList<>();
        listSubjects.forEach(subject-> {
            Enrollment enrollment = new Enrollment();
            enrollment.setSubject(subject);
            enrollment.setUser(user);
            listEnrollments.add(enrollment);
        });
        enrollmentRepository.saveAll(listEnrollments);
        user.setSetup(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public List<Subject> getAllEnrollmentSubjects() {
        return subjectRepository.getAllEnrollmentSubjects(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public boolean deleteEnrollmentSubjects(List<Long> subjects) {
        if(subjects.size() == 0) return false;
        Enrollment enrollment = enrollmentRepository.findBySubjectIdAndUserEmail(subjects.get(0), SecurityContextHolder.getContext().getAuthentication().getName());
        if (enrollment == null) return false;
        enrollmentRepository.delete(enrollment);
        return true;
    }
}
