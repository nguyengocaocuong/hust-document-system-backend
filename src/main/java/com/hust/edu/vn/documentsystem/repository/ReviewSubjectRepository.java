package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.ReviewSubject;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewSubjectRepository extends JpaRepository<ReviewSubject, Long> {
    List<ReviewSubject> findByIsDone(boolean b);

    List<ReviewSubject> findByOwner(User user);

    ReviewSubject findByIdAndIsDone(Long reviewId, boolean b);


    ReviewSubject findByIdAndIsHidden(Long id, boolean hidden);

    ReviewSubject findByIdAndIsDoneOrOwner(Long reviewId, boolean b, User user);
}