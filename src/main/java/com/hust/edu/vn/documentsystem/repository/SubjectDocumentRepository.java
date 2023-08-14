package com.hust.edu.vn.documentsystem.repository;

import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface SubjectDocumentRepository extends JpaRepository<SubjectDocument, Long> {

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
            AND (
                    sd.owner.id = :userId 
                OR
                    sd.isPublic = true 
                OR 
                    sp.id IS NOT NULL 
                AND 
                    sp.user.id = :userId
                )
            """)
    List<SubjectDocument> findAllSubjectDocumentCanAccessByUserEmail(@Param("subjectId") Long subjectId,@Param("userId") Long userId);

    List<SubjectDocument> findAllByIsDeleteAndOwner(boolean b, User user);

    SubjectDocument findByIdAndIsDelete(Long subjectDocumentId, boolean b);

    @Query(value = "SELECT s FROM SubjectDocument s WHERE s.isDelete = false AND s.owner.email = :name")
    List<SubjectDocument> findAllByUserEmailAndIsDelete(String name);

    @Query(value = "SELECT s FROM SubjectDocument s WHERE s.id =:subjectDocumentId AND s.isDelete = false AND s.isPublic = :isPublic AND s.owner.email = :name")
    SubjectDocument findByIdAndUserEmailAndIsPublic(Long subjectDocumentId, String name, boolean isPublic);


    @Query("""
            SELECT sd
            FROM SubjectDocument sd
            LEFT JOIN SharePrivate sp
            ON sd.id = sp.subjectDocument.id
            WHERE sd.owner.id = :userId AND (sd.isPublic = true OR sp.id IS NOT NULL AND sp.user.email = :email)
            """)
    List<SubjectDocument> getAllSharedByUser(Long userId, String email);

    @Query("""
            SELECT sd
            FROM SubjectDocument sd
            LEFT JOIN SharePrivate sp
            ON sd.id = sp.subjectDocument.id 
            WHERE sd.id = :subjectDocumentId 
            AND (
                sd.owner.id = :userId 
                OR sd.isPublic = true 
                OR 
                    (
                        sp.id IS NOT NULL 
                        AND sp.user.id = :userId
                    )
            )
            """)
    SubjectDocument findByIdAndUserEmailCanDownload(Long subjectDocumentId, Long userId);


    @Query("""
                SELECT sd
                FROM SubjectDocument sd
                LEFT JOIN SharePrivate sp
                ON sd.id = sp.subjectDocument.id
                LEFT JOIN ShareByLink sl
                ON sd.id = sl.subjectDocument.id
                WHERE sd.id = :subjectDocumentId
                AND sd.isDelete = false 
                AND 
                    (
                        sd.isPublic = true 
                        OR sd.owner.id = :userId 
                        OR (sp.id IS NOT NULL 
                        AND sp.user.id = :userId )
                        OR (sl.id IS NOT NULL 
                        AND sl.token = :token)
                    )
            """)
    SubjectDocument findByIdAndUserEmailAndToken(Long subjectDocumentId, Long userId, String token);

    @Query(value = """
              WITH searched_ids AS (
                SELECT
                    sd.id
                FROM
                    subject_documents sd
                    LEFT JOIN share_privates sp ON sd.id = sp.subject_document_id
                    JOIN subjects s ON sd.subject_id = s.id
                    JOIN institutes i ON i.id = s.institute_id
                WHERE
                    (
                        :instituteSize = 0
                        OR i.id IN (:institute)
                    )
                    AND (
                        :subjectDocumentTypeSize = 0
                        OR sd.subject_document_type_id IN (:subjectDocumentType)
                    )
                    AND (
                        :subjectSize = 0
                        OR sd.subject_id IN (:subject)
                    )
                    AND (
                        :semesterSize = 0
                        OR sd.semester IN (:semester)
                    )
                    AND (
                        sd.is_public = true
                        OR sd.owner_id = :userId
                        OR sp.id IS NOT NULL
                        AND sp.user_id = :userId
                    )
                    AND (
                        :key IS NULL
                        OR (
                            :fuzzySearch = false AND (
                                :deepSearch = false AND sd.description_no_diacritics ILIKE concat('%', :key, '%')
                                OR :deepSearch = true AND sd.description_en ILIKE concat('%', :key, '%')
                            )
                            OR :fuzzySearch = true AND (
                                :deepSearch = false AND ts_v @@ to_tsquery(
                                    'english',
                                    (
                                        SELECT
                                            string_agg(part, ' | ') AS trigram_sequence
                                        FROM
                                            (
                                                SELECT
                                                    token AS part
                                                FROM
                                                    regexp_split_to_table(:key, ' ') AS token
                                            ) AS trigrams
                                    )
                                )
                                OR :deepSearch = true
                                AND ts @@ to_tsquery(
                                    'english',
                                    (
                                        SELECT
                                            string_agg(part, ' & ') AS trigram_sequence
                                        FROM
                                            (
                                                SELECT
                                                    token AS part
                                                FROM
                                                    regexp_split_to_table(:key, ' ') AS token
                                            ) AS trigrams
                                    )
                                )
                            )
                        )
                    )
                    AND sd.is_delete = false
            )
               SELECT sd.id,
                                             sd.type,
                                             sd.description,
                                             sd.created_at,
                                             sd.semester,
                                             sd.total_download,
                                             sd.total_view,
                                             sd.subject_document_type_id,
                                             sd.subject_id,
                                             sd.owner_id,
                                             sd.document_id,
                                             COUNT(DISTINCT csd.id),
                                             COUNT(DISTINCT asd.id),
                                             COUNT(DISTINCT fsd.id)
            FROM
                subject_documents sd
                LEFT JOIN comment_subject_documents csd on sd.id = csd.subject_document_id
                LEFT JOIN answer_subject_documents asd on sd.id = asd.subject_document_id
                LEFT JOIN favorite_subject_documents fsd on sd.id = fsd.subject_document_id
                JOIN documents d ON sd.document_id = d.id
                JOIN subject_document_types sdt ON sd.subject_document_type_id = sdt.id
            WHERE
                sd.id IN (
                    SELECT
                        id
                    FROM
                        searched_ids
                )
            GROUP BY
                sd.id
                                                        """, nativeQuery = true)
    List<Object[]> findAllSubjectDocumentBySearchOption(String key, List<String> semester, int semesterSize, List<Long> subjectDocumentType, int subjectDocumentTypeSize, List<Long> subject, int subjectSize, List<Long> institute, int instituteSize, Long userId, boolean deepSearch, boolean fuzzySearch);

}