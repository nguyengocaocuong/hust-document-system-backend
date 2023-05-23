package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.SubjectRepository;
import com.hust.edu.vn.documentsystem.repository.TeacherRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.TeacherService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;

    private final ModelMapperUtils modelMapperUtils;
    private final UserRepository userRepository;

    private final GoogleCloudStorageService googleCloudStorageService;
    private final SubjectRepository subjectRepository;

    @Autowired
    public TeacherServiceImpl(
            TeacherRepository teacherRepository,
            ModelMapperUtils modelMapperUtils,
            UserRepository userRepository,
            GoogleCloudStorageService googleCloudStorageService,
            SubjectRepository subjectRepository
    ) {
        this.teacherRepository = teacherRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.userRepository = userRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public Teacher getTeacherById(Long id) {
        return teacherRepository.findById(id).orElse(null);
    }

    @Override
    public Teacher createTeacher(TeacherModel teacherModel) {
        if (teacherRepository.findByEmail(teacherModel.getEmail()) != null) return null;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        Teacher teacher = modelMapperUtils.mapAllProperties(teacherModel, Teacher.class);
        teacher.setOwner(user);
        return teacherRepository.save(teacher);
    }

    @Override
    public List<Teacher> findTeacherByKeyword(String keyword) {
        return teacherRepository.searchTeacherByKeyword(keyword);
    }

    @Override
    public boolean updateTeacher(TeacherModel teacherModel) {
        if (teacherModel.getId() == null) {
            return false;
        }
        Teacher teacher = teacherRepository.findById(teacherModel.getId()).orElse(null);
        if (teacher == null || !teacher.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        teacher.setName(teacherModel.getName());
        teacher.setEmail(teacherModel.getEmail());
        teacher.setPhoneNumber(teacherModel.getPhoneNumber());
        teacher.setDob(teacherModel.getDob());
        teacher.setDescription(teacherModel.getDescription());
        if (teacherModel.getAvatarFile() != null) {
            try {
                String url = googleCloudStorageService.uploadAvatarToGCP(teacherModel.getAvatarFile(), teacher.getOwner().getRootPath());
                teacher.setAvatar(url);
            } catch (IOException e) {
                return false;
            }
        }
        teacherRepository.save(teacher);
        return true;
    }

    @Override
    public boolean deleteTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        if (teacher == null || !teacher.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        teacherRepository.deleteById(teacherId);
        return true;
    }

    @Override
    public List<Teacher> getAllTeachersCreateByUser() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return teacherRepository.findAllByOwner(user);
    }

    @Override
    public boolean deleteSubject(TeacherModel teacherModel) {
        Teacher teacher = teacherRepository.findById(teacherModel.getId()).orElse(null);
        if (teacher == null)
            return false;
        Subject subject = subjectRepository.findById(teacherModel.getSubjectId()).orElse(null);
        if (subject == null || teacher.getSubjects().contains(subject)) return false;
        teacher.getSubjects().remove(subject);
        teacherRepository.save(teacher);
        return true;
    }

    @Override
    public boolean addSubjects(TeacherModel teacherModel) {
        Teacher teacher = teacherRepository.findById(teacherModel.getId()).orElse(null);
        if (teacher == null)
            return false;
        Subject subject = subjectRepository.findById(teacherModel.getSubjectId()).orElse(null);
        if (subject == null || !teacher.getSubjects().contains(subject)) return false;
        teacher.getSubjects().add(subject);
        teacherRepository.save(teacher);
        return true;
    }

    @Override
    public List<TeacherDto> getAllTeachersForFilter() {
        return teacherRepository.findAllTeacherForFilter();
    }
}
