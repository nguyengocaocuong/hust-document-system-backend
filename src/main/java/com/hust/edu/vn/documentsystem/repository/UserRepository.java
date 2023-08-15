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

    @Query("SELECT DATE(u.createdAt) AS date, COUNT(u.id) " +
            "FROM User u " +
            "WHERE u.createdAt >= :startDate AND u.roleType = 'USER'" +
            "GROUP BY date " +
            "ORDER BY DATE(u.createdAt) DESC")
    List<Object[]> getUserForDashboard(Date startDate);


    @Query(value = "SELECT u FROM User u WHERE u.createdAt >= :startDate AND u.roleType = 'USER'")
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

    @Query(value = """
            SELECT sd
            FROM SubjectDocument sd
            LEFT JOIN SharePrivate sp
            ON sd.id = sp.subjectDocument.id
            LEFT JOIN Subject s
            ON sd.subject.id = s.id
            LEFT JOIN FavoriteSubject fs
            ON fs.subject.id = s.id
            AND fs.user.id = :userId
            WHERE sd.owner.id != :userId 
            AND (sd.isPublic = true OR sp.user.id = :userId)
            ORDER BY CASE WHEN sd.subject.id IN (:preSubjectId) THEN 1 END,fs.id, sd.id DESC
            """
    )
    List<SubjectDocument> getSubjectDocumentForRecommend(Long userId, PageRequest pageRequest, List<Long> preSubjectId);


    @Query("""
   SELECT  fs.subject.id
    FROM FavoriteSubject fs
    WHERE fs.user.id = :userId AND fs.createAt BETWEEN :endDate AND :startDate
    GROUP BY fs.subject.id
    ORDER BY COUNT(fs.id) DESC
    LIMIT 3
""")
    List<Long> getCurrentAccessSubject(Long userId, Date startDate, Date endDate);

    @Query("""
   SELECT s.id
    FROM History h
    JOIN SubjectDocument sd
    ON sd.id = h.subjectDocument.id
    JOIN Subject s
    ON s.id = sd.subject.id
    WHERE h.user.id = :userId AND h.createdAt BETWEEN :endDate AND :startDate
    GROUP BY s.id
    ORDER BY COUNT(s.id) DESC
    LIMIT 3
""")
    List<Long> getCurrentView(Long userId, Date startDate, Date endDate);

    @Query("""
            SELECT DISTINCT s.id
            FROM Subject s
            JOIN Enrollment e
            ON e.subject.id = s.id
            WHERE e.user.id = :userId
            """)
    List<Long> getEnrollmentSubject(Long userId);
}