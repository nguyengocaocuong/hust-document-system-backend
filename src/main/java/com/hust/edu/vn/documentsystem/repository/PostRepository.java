package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.data.dto.PostDto;
import com.hust.edu.vn.documentsystem.entity.Post;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByOwner(User byEmail);

    Post findByIdAndIsDone(Long id, boolean done);

    Post findByIdAndIsHidden(Long id, boolean b);


    @Query("SELECT p, COUNT(DISTINCT fp.id), COUNT(DISTINCT ap.id), COUNT(DISTINCT cp.id) " +
            "FROM Post p LEFT JOIN FavoritePost fp " +
            "ON p.id = fp.post.id LEFT JOIN AnswerPost ap " +
            "ON p.id = ap.post.id LEFT JOIN CommentPost cp " +
            "ON p.id = cp.post.id " +
            "WHERE p.isDone = true AND p.isHidden = false " +
            "GROUP BY p.id " +
            "ORDER BY p.createdAt " +
            "LIMIT 10")
    List<Object[]> getPostForHomePage();

    @Query(value = "SELECT p FROM Post p WHERE p.owner.email = :email")
    List<Post> findAllPostCreatedByUser(String email);

    @Query(value = "SELECT p FROM Post p WHERE p.id = :postId AND p.owner.email = :email")
    Post findByIdAndUserEmail(Long postId, String email);

    @Query("SELECT DATE(u.createdAt) AS date, COUNT(u.id) " +
            "FROM Post u " +
            "WHERE u.createdAt >= :startDate " +
            "GROUP BY date " +
            "ORDER BY DATE(u.createdAt) DESC")
    List<Object[]> getPostForDashboard(Date startDate);
}