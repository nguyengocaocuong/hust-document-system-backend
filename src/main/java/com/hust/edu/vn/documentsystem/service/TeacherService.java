package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.data.model.TeacherModel;
import com.hust.edu.vn.documentsystem.entity.Teacher;

import java.util.List;

public interface TeacherService {
    List<Teacher> getAllTeachers();

    Teacher getTeacherById(Long id);

    Teacher createTeacher(TeacherModel teacherModel);

    List<Teacher> findTeacherByKeyword(String keyword);

    boolean updateTeacher(TeacherModel teacherModel);

    boolean deleteTeacher(Long teacherId);

    List<Teacher> getAllTeachersCreateByUser();

    boolean deleteSubject(TeacherModel teacherModel);

    boolean addSubjects(TeacherModel teacherModel);

    List<TeacherDto> getAllTeachersForFilter();
}
