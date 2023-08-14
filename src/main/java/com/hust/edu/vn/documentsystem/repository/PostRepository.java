package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.dto.PostInfoDto;
import com.hust.edu.vn.documentsystem.entity.Post;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByOwner(User byEmail);

    Post findByIdAndIsDone(Long id, boolean done);

    Post findByIdAndIsHidden(Long id, boolean b);

    @Query(value = """
            SELECT  new com.hust.edu.vn.documentsystem.dto.PostInfoDto(
            p, 
            COUNT(ap.id), 
            COUNT(cp.id), 
            COUNT(fp.id),  
            CASE WHEN EXISTS (SELECT 1 FROM FavoritePost fp_inner JOIN User u ON fp_inner.user.id = u.id  WHERE fp_inner.post.id = p.id AND u.email = :email) THEN true ELSE false END, 
            (SELECT s_inner.name FROM Subject s_inner WHERE s_inner.id = p.subject.id),  
            (SELECT d_inner.path FROM Document d_inner WHERE d_inner.id = p.document.id))
            FROM Post p
            LEFT JOIN AnswerPost ap
            ON p.id = ap.post.id
            LEFT JOIN CommentPost cp
            ON p.id = cp.post.id
            LEFT JOIN FavoritePost fp
            ON p.id = fp.post.id
            GROUP BY p.id
            """)
    List<PostInfoDto> getPostForHomePage(String email,PageRequest pageRequest);

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