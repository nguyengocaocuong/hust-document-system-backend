package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.History;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}