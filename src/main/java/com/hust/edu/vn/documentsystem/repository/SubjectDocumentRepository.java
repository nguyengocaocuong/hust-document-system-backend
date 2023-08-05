package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface SubjectDocumentRepository extends JpaRepository<SubjectDocument, Long> {

    @Query(value = "SELECT DISTINCT sd.subjectDocumentType FROM SubjectDocument AS sd")
    List<SubjectDocumentType> findAllSubjectDocumentType();

    @Query(value = "SELECT sd FROM SubjectDocument sd WHERE sd.owner.email = :email")
    List<SubjectDocument> findAllByUserEmail(String email);

    @Query(value = "SELECT s FROM SubjectDocument s WHERE s.id = :subjectDocumentId AND s.owner.email = :name")
    SubjectDocument findByIdAndUserEmail(Long subjectDocumentId, String name);

    @Query("""
            SELECT DISTINCT sd FROM SubjectDocument sd
            LEFT JOIN SharePrivate sp
            ON sd.id = sp.subjectDocument.id
            WHERE sd.isDelete = false 
            AND sd.subject.id = :subjectId 
            AND (sd.isPublic = true OR sp.id IS NOT NULL AND sp.user.id = :userId)
            """)
    List<SubjectDocument> findAllSubjectDocumentCanAccessByUserEmail(@Param("userId") Long userId, @Param("subjectId") Long subjectId);

    List<SubjectDocument> findAllByIsDeleteAndOwner(boolean b, User user);

    SubjectDocument findByIdAndIsDelete(Long subjectDocumentId, boolean b);

    @Query(value = "SELECT s FROM SubjectDocument s WHERE s.isDelete = false AND s.owner.email = :name")
    List<SubjectDocument> findAllByUserEmailAndIsDelete(String name);

    @Query(value = "SELECT s FROM SubjectDocument s WHERE s.id =:subjectDocumentId AND s.isDelete = false AND s.isPublic = :isPublic AND s.owner.email = :name")
    SubjectDocument findByIdAndUserEmailAndIsPublic(Long subjectDocumentId, String name, boolean isPublic);
}