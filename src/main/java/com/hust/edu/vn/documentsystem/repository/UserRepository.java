package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
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
}