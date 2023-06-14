package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReviewSubjectRepository extends JpaRepository<ReviewSubject, Long> {

    List<ReviewSubject> findByOwner(User user);

    ReviewSubject findByIdAndIsHidden(Long id, boolean hidden);

    ReviewSubject findByIdAndIsDoneOrOwner(Long reviewId, boolean b, User user);

    List<ReviewSubject> findAllByIsDone(boolean isDone);

    List<ReviewSubject> findAllByOwner(User user);

    @Query(value = "SELECT rs FROM ReviewSubject rs WHERE rs.id = :reviewSubjectId AND rs.owner.email = :email")
    ReviewSubject findByIdAndUserEmail(Long reviewSubjectId, String email);

    @Query(value = "SELECT rs FROM ReviewSubject rs WHERE rs.owner.email = :email")
    List<ReviewSubject> findAllReviewSubjectCreatedByUser(String email);

    @Query(value = "SELECT rs FROM ReviewSubject  rs WHERE rs.isDone = true AND rs.approved = 'NEW'")
    List<ReviewSubject> findAllNewReviewSubject();

    @Query("SELECT DATE(u.createdAt) AS date, COUNT(u.id) " +
            "FROM ReviewSubject u " +
            "WHERE u.createdAt >= :startDate " +
            "GROUP BY date " +
            "ORDER BY DATE(u.createdAt) DESC")
    List<Object[]> getReviewForDashboard(Date startDate);
}