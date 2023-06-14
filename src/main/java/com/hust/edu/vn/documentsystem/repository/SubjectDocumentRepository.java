package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectDocumentRepository extends JpaRepository<SubjectDocument, Long> {

    @Query(value = "SELECT DISTINCT sd.subjectDocumentType FROM SubjectDocument AS sd")
    List<SubjectDocumentType> findAllSubjectDocumentType();

    @Query(value = "SELECT DISTINCT sd.semester FROM SubjectDocument AS sd")
    List<String> findAllSemester();

    @Query(value = "SELECT sd FROM SubjectDocument sd WHERE sd.owner.email = :email")
    List<SubjectDocument> findAllByUserEmail(String email);

    @Query(value = "SELECT s FROM SubjectDocument s WHERE s.id = :subjectDocumentId AND s.owner.email = :name")
    SubjectDocument findByIdAndUserEmail(Long subjectDocumentId, String name);

    @Query("SELECT DISTINCT sd FROM SubjectDocument sd " +
            "JOIN SharePrivate sp " +
            "ON sd.id = sp.subjectDocument.id  " +
            "OR sd.isPublic = true " +
            "OR sd.owner.id = :userId " +
            "OR sp.user.id = :userId " +
            "WHERE sd.isDelete = false AND sd.subject.id = :subjectId")
    List<SubjectDocument> findAllSubjectDocumentCanAccessByUserEmail(@Param("userId") Long userId, @Param("subjectId") Long subjectId);

    List<SubjectDocument> findAllByIsDeleteAndOwner(boolean b, User user);

    SubjectDocument findByIdAndIsDelete(Long subjectDocumentId, boolean b);

    @Query(value = "SELECT s FROM SubjectDocument s WHERE s.isDelete = false AND s.owner.email = :name")
    List<SubjectDocument> findAllByUserEmailAndIsDelete(String name);

    @Query(value = "SELECT s FROM SubjectDocument s WHERE s.id =:subjectDocumentId AND s.isDelete = false AND s.isPublic = :isPublic AND s.owner.email = :name")
    SubjectDocument findByIdAndUserEmailAndIsPublic(Long subjectDocumentId, String name, boolean isPublic);
}