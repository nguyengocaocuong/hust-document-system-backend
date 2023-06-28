package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.data.dto.TeacherDto;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByEmailHust(String email);

    @Query(value = "SELECT new com.hust.edu.vn.documentsystem.data.dto.TeacherDto(t.id, t.name) FROM Teacher AS t")
    List<TeacherDto> findAllTeacherForFilter();

    @Query(value = "" +
            "SELECT t, COUNT(DISTINCT rt.id) " +
            "FROM Teacher t " +
            "LEFT JOIN ReviewTeacher rt " +
            "ON t.id = rt.teacher.id " +
            "GROUP BY t.id")
    List<Object[]> getAllTeacherForAdmin();
}