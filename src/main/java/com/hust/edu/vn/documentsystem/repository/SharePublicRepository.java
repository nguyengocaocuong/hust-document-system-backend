package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.SharePublic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharePublicRepository extends JpaRepository<SharePublic, Long> {
}