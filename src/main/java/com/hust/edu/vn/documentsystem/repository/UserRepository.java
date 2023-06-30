package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.AnswerSubjectDocument;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findByRoleType(String admin);

    @Query("SELECT DATE(u.createdAt) AS date, COUNT(u.id) " +
            "FROM User u " +
            "WHERE u.createdAt >= :startDate " +
            "GROUP BY date " +
            "ORDER BY DATE(u.createdAt) DESC")
    List<Object[]> getUserForDashboard(Date startDate);


    @Query(value = "SELECT u FROM User u WHERE u.createdAt >= :startDate")
    List<User> getAllNewUser(Date startDate);

    @Query(value = "" +
            "SELECT u, COUNT(DISTINCT p.id), COUNT(DISTINCT rt.id) + COUNT(DISTINCT rs.id) " +
            "FROM User u " +
            "LEFT JOIN Post p " +
            "ON u.id = p.owner.id " +
            "LEFT JOIN ReviewTeacher rt " +
            "ON u.id = rt.owner.id " +
            "LEFT JOIN ReviewSubject rs " +
            "ON u.id = rs.owner.id " +
            "WHERE u.id = :userId ")
    Object[] getProfileUser(Long userId);
    @Query(value = "" +
            "SELECT  sd.subjectDocumentType, COUNT(DISTINCT sd.id) " +
            "FROM User u " +
            "JOIN SubjectDocument sd " +
            "ON u.id = sd.owner.id " +
            "WHERE u.id = :userId " +
            "GROUP BY sd.subjectDocumentType")
    Object[] getSubjectDocumentForProfile(Long userId);

    @Query(value = "" +
            "SELECT sd " +
            "FROM SubjectDocument sd " +
            "LEFT JOIN Subject s " +
            "ON sd.subject.id = s.id " +
            "LEFT JOIN FavoriteSubject fs " +
            "ON fs.subject.id = s.id " +
            "AND fs.user.id = :userId " +
            "WHERE sd.owner.id != :userId " +
            "ORDER BY fs.id, sd.id DESC "
    )
    List<SubjectDocument> getSubjectDocumentForRecommend(Long userId, PageRequest pageRequest);

    @Query(value = "" +
            "SELECT asd " +
            "FROM AnswerSubjectDocument asd " +
            "LEFT JOIN SubjectDocument sd " +
            "ON sd.id = asd.subjectDocument.id " +
            "LEFT JOIN FavoriteSubjectDocument fsd " +
            "ON fsd.subjectDocument.id = sd.id " +
            "AND fsd.user.id = :userId " +
            "WHERE asd.owner.id != :userId " +
            "ORDER BY fsd.id, asd.id DESC "
    )
    List<AnswerSubjectDocument> getAnswerSubjectDocumentForRecommend(Long userId, PageRequest pageRequest);
}