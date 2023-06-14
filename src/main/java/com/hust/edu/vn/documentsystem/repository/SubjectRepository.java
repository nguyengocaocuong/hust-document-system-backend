package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
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

    @Query(value =
            "SELECT new com.hust.edu.vn.documentsystem.data.dto.SubjectDto(s, " +
                    "count ( DISTINCT sd.id), " +
                    "count (DISTINCT csd.id), " +
                    "count(DISTINCT f.id), " +
                    "count(DISTINCT asd.id)" +
                    ") " +
                    "FROM Subject s " +
                    "LEFT JOIN SubjectDocument sd " +
                    "ON s.id = sd.subject.id AND sd.isDelete = false " +
                    "AND (" +
                            "sd.isPublic = true " +
                            "OR sd.owner.id = :userId " +
                            "OR sd.id IN (" +
                            "SELECT sd.id " +
                            "FROM SubjectDocument  sd " +
                            "JOIN SharePrivate sp " +
                            "ON sd.id = sp.subjectDocument.id " +
                            "AND sp.user.id = :userId" +
                        ")" +
                    ")" +
                    "LEFT JOIN CommentSubjectDocument csd " +
                    "ON sd.id = csd.subjectDocument.id " +
                    "LEFT JOIN FavoriteSubject f " +
                    "ON f.subject.id = s.id " +
                    "LEFT JOIN AnswerSubjectDocument asd " +
                    "ON asd.subjectDocument.id = sd.id " +
                    "GROUP BY s.id")
    List<SubjectDto> findAllSubjects(@Param("userId") Long userId);


    @Query(value = "SELECT s, COUNT(DISTINCT sd.id), COUNT(DISTINCT fs.id), COUNT(DISTINCT rs.id) FROM Subject s " +
            "LEFT JOIN SubjectDocument sd " +
            "ON sd.subject.id = s.id " +
            "LEFT JOIN FavoriteSubject fs " +
            "ON s.id = fs.subject.id " +
            "LEFT JOIN ReviewSubject rs " +
            "ON s.id = rs.subject.id " +
            "GROUP BY s.id")
    List<Object[]> getAllSubjectForAdmin();
}