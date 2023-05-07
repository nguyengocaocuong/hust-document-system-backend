package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReviewTeacher;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewTeacherRepository extends JpaRepository<ReviewTeacher, Long> {
    @Query("SELECT rt FROM ReviewTeacher rt INNER JOIN rt.teacher t INNER JOIN t.subjects s WHERE (:teacherId IS NULL OR t.id = :teacherId) AND (:subjectId IS NULL OR s.id = :subjectId)")
    List<ReviewTeacher> findReviewsByTeacherIdAndSubjectId(@Param("teacherId") Long teacherId, @Param("subjectId") Long subjectId);

    List<ReviewTeacher> findByOwner(User byEmail);

    List<ReviewTeacher> findByDone(boolean done);

    ReviewTeacher findByIdAndDone(Long reviewId, boolean done);

    ReviewTeacher findByIdAndIsHidden(Long id, boolean b);
}