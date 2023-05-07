package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.Post;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByOwner(User byEmail);

    List<Post> findByIsDone(boolean done);

    Post findByIdAndIsDone(Long id, boolean done);

    Post findByIdAndIsHidden(Long id, boolean b);
}