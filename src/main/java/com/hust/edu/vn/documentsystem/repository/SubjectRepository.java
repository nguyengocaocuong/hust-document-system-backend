package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.Teacher;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByOwner(User user);

    @Query(value = "SELECT new com.hust.edu.vn.documentsystem.data.dto.SubjectDto(s.id, s.name, s.subjectCode, s.institute) FROM Subject AS s")
    List<SubjectDto> findAllSubjectName();


    @Query(value = "SELECT s, COUNT(DISTINCT sd.id), COUNT(DISTINCT fs.id), COUNT(DISTINCT rs.id) FROM Subject s " +
            "LEFT JOIN SubjectDocument sd " +
            "ON sd.subject.id = s.id " +
            "LEFT JOIN FavoriteSubject fs " +
            "ON s.id = fs.subject.id " +
            "LEFT JOIN ReviewSubject rs " +
            "ON s.id = rs.subject.id " +
            "GROUP BY s.id")
    List<Object[]> getAllSubjectForAdmin();

    @Query(value =
            """
                    SELECT new com.hust.edu.vn.documentsystem.data.dto.SubjectDto(s,
                                       count(DISTINCT (CASE WHEN (sd.id IS NOT NULL AND sd.isDelete = false AND (sd.isPublic = true OR sd.owner.id = :userId OR (sp.id IS NOT NULL AND sp.user.id = :userId))) THEN sd.id END)),
                                       count(DISTINCT (CASE WHEN (sd.id IS NOT NULL AND sd.isDelete = false AND (sd.isPublic = true OR sd.owner.id = :userId OR (sp.id IS NOT NULL AND sp.user.id = :userId))) THEN sd.id END)),
                                        count(DISTINCT f.id),
                                        count(DISTINCT f.id)
                    )
                                        FROM Subject s
                                        LEFT JOIN SubjectDocument sd
                                        ON s.id = sd.subject.id
                                        LEFT JOIN SharePrivate  sp
                                        ON sd.id = sp.subjectDocument.id
                                        LEFT JOIN FavoriteSubject f
                                        ON f.subject.id = s.id
                                        GROUP BY s.id
                    """)
    List<SubjectDto> findAllSubjects(@Param("userId") Long userId, PageRequest pageRequest);

    @Query("""
                SELECT s
                FROM Subject s
                WHERE s.institute.id = :instituteId
            """)
    List<Subject> findAllByInstitute(Long instituteId);

    @Query("""
                SELECT DISTINCT  s.institute
                FROM Subject s
            """)
    List<String> getAllInstitute();

    @Query("""
            SELECT s
            FROM Subject s
            WHERE s.id IN :subjects
            """
    )
    List<Subject> findAllByListId(List<Long> subjects);

    @Query("""
            SELECT s
            FROM Subject s
            JOIN Enrollment e
            ON s.id = e.subject.id
            WHERE e.user.email = :email
            """)
    List<Subject> getAllEnrollmentSubjects(String email);

    @Query("""
            SELECT s
            FROM Subject s
            JOIN Enrollment e
            ON s.id = e.subject.id
            WHERE e.user.id = :userId
            """)
    List<Subject> getAllEnrollmentSubjectsByUserId(Long userId);
}