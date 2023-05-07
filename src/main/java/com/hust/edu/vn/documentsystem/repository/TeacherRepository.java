package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("SELECT t FROM Teacher t WHERE t.name LIKE %:keyword% OR (t.description IS NOT NULL AND t.description LIKE %:keyword%)")
    List<Teacher> searchTeacherByKeyword(@Param("keyword") String keyword);

    List<Teacher> findAllByOwner(User user);

    Teacher findByEmail(String email);
}