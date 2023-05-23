package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
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
    List<Subject> findByKeywordAndTeachers(@Param("keyword") String keyword, @Param("teacher") Teacher teacher);

    @Query(value = "SELECT new com.hust.edu.vn.documentsystem.data.dto.SubjectDto(s.id, s.name) FROM Subject AS s")
    List<SubjectDto> findAllSubjectName();

    @Query(value = "SELECT new com.hust.edu.vn.documentsystem.data.dto.SubjectDto(s, count ( DISTINCT sd.id), count (DISTINCT csd.id), count(DISTINCT f.id), count(DISTINCT asd.id)) FROM Subject s JOIN SubjectDocument sd ON s = sd.subject LEFT JOIN CommentSubjectDocument as csd ON sd = csd.subjectDocument LEFT JOIN FavoriteSubject f ON f.subject = s LEFT JOIN AnswerSubjectDocument asd ON asd.subjectDocument = sd GROUP BY s")
    List<SubjectDto> findAllSubjects();

}