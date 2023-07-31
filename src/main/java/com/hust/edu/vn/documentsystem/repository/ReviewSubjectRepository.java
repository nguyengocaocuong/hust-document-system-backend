package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.common.type.ApproveType;
import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.Date;
import java.util.List;

public interface ReviewSubjectRepository extends JpaRepository<ReviewSubject, Long> {

    List<ReviewSubject> findAllByDone(boolean isDone);


    @Query(value = "SELECT rs FROM ReviewSubject rs WHERE rs.owner.email = :email")
    List<ReviewSubject> findAllReviewSubjectCreatedByUser(String email);

    @Query(value = "SELECT rs FROM ReviewSubject  rs WHERE rs.done = true AND rs.approved = 'NEW'")
    List<ReviewSubject> findAllNewReviewSubject();

    @Query("""
            SELECT DATE(u.createdAt) AS date, COUNT(u.id)
            FROM ReviewSubject u
            WHERE u.createdAt >= :startDate
            GROUP BY date
            ORDER BY DATE(u.createdAt) DESC
            """)
    List<Object[]> getReviewForDashboard(Date startDate);

    @Query(value = """
            SELECT rs
            FROM ReviewSubject rs
            WHERE rs.id = :reviewSubjectId
            AND rs.subject.id = :subjectId
            AND rs.owner.email = :email 
            """)
    ReviewSubject findByIdAndSubjectIdAndUserEmail(Long reviewSubjectId, Long subjectId, String email);

    ReviewSubject findByIdAndApproved(Long reviewSubjectId, ApproveType approveType);
}