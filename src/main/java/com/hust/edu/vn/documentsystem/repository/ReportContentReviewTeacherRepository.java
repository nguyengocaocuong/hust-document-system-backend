package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReportContentReviewTeacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ReportContentReviewTeacherRepository extends JpaRepository<ReportContentReviewTeacher, Long> {
    @Query("""
            SELECT r
            FROM ReportContentReviewTeacher r
            WHERE r.owner.email = :email
            """)
    List<ReportContentReviewTeacher> getAllReported(String email);

    @Query("""
            SELECT r
            FROM ReportContentReviewTeacher r
            WHERE r.owner.email = :email AND r.id = :reportContentReviewTeacherId AND r.reviewTeacher.id = :reviewTeacherId
            """)
    ReportContentReviewTeacher findByTeacherIdAndIdAndOwnerEmail(Long reportContentReviewTeacherId, Long reviewTeacherId, String email);
}