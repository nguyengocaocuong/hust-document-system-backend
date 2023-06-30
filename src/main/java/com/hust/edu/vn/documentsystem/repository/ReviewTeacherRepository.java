package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReviewTeacherRepository extends JpaRepository<ReviewTeacher, Long> {

    @Query("""
            SELECT rt
            FROM ReviewTeacher rt
            WHERE rt.done = true
            AND rt.hidden = false
            ORDER BY rt.createdAt
            """)
    List<ReviewTeacher> findAllReviewTeacher();

    @Query(value = """
            SELECT rt FROM
            ReviewTeacher rt
            WHERE rt.id = :reviewTeacherId
            AND rt.owner.email = :email
            """)
    ReviewTeacher findByIdAndUserEmail(Long reviewTeacherId, String email);

    @Query(value = """
            SELECT rt
            FROM ReviewTeacher rt
            WHERE rt.owner.email = :email
            """)
    List<ReviewTeacher> findAllReviewTeacherCreateByUser(String email);

    @Query(value = """
            SELECT rt
            FROM ReviewTeacher rt
            WHERE rt.done = true
            AND rt.approved = 'NEW'
            """)
    List<ReviewTeacher> findAllNewReviewTeacher();

    @Query("""
            SELECT DATE(u.createdAt) AS date, COUNT(u.id)
            FROM ReviewTeacher u
            WHERE u.createdAt >= :startDate
            GROUP BY date
            ORDER BY DATE(u.createdAt) DESC
            """)
    List<Object[]> getReviewForDashboard(Date startDate);

    @Query("""
            SELECT rt
            FROM ReviewTeacher rt
            WHERE rt.id = :reviewTeacherId
            AND rt.teacher.id = :teacherId
            AND rt.owner.email = :email
            """)
    ReviewTeacher findByIdAndTeacherIdAndOwnerEmail(Long reviewTeacherId, Long teacherId, String email);
}