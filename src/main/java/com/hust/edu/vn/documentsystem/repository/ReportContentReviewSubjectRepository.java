package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReportContentReviewSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ReportContentReviewSubjectRepository extends JpaRepository<ReportContentReviewSubject, Long> {
    @Query("""
            SELECT r
            FROM ReportContentReviewSubject r
            WHERE r.owner.email = :email
            """)
    List<ReportContentReviewSubject> getAllReported(String email);

    @Query("""
            SELECT r
            FROM ReportContentReviewSubject r
            WHERE r.owner.email = :email AND r.id = :reportContentReviewSubjectId AND r.reviewSubject.id = :reviewSubjectId
            """)
    ReportContentReviewSubject findByTeacherIdAndIdAndOwnerEmail(Long reportContentReviewSubjectId, Long reviewSubjectId, String email);
}