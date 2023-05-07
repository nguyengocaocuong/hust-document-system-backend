package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByOwner(User user);
    @Query("SELECT s FROM Subject s WHERE s.name LIKE %:keyword% AND (:teacher IS NULL OR :teacher MEMBER OF s.teachers) ")
    List<Subject> findByKeywordAndTeachers(@Param("keyword") String keyword,@Param("teacher") Teacher teacher);

}