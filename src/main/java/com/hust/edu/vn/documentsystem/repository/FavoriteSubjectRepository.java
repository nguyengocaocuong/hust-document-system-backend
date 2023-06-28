package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.FavoriteSubject;
import com.hust.edu.vn.documentsystem.entity.Subject;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteSubjectRepository extends JpaRepository<FavoriteSubject, Long> {
    List<FavoriteSubject> findByUser(User user);

    FavoriteSubject findByUserAndSubject(User user, Subject subject);

    @Query(value = "SELECT fs FROM FavoriteSubject fs WHERE fs.subject.id = :subjectId")
    List<FavoriteSubject> findAllBySubjectId(Long subjectId);
}