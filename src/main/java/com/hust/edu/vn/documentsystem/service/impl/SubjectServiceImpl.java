package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.storage.Acl;
import com.hust.edu.vn.documentsystem.common.type.DocumentType;
import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import com.hust.edu.vn.documentsystem.common.type.SubjectDocumentType;
import com.hust.edu.vn.documentsystem.common.type.TargetLanguageType;
import com.hust.edu.vn.documentsystem.data.dto.SubjectDto;
import com.hust.edu.vn.documentsystem.data.model.*;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.event.NotifyEvent;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudTranslateService;
import com.hust.edu.vn.documentsystem.service.SubjectService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SubjectServiceImpl implements SubjectService {
    private final CommentSubjectDocumentRepository commentSubjectDocumentRepository;
    private final FavoriteSubjectRepository favoriteSubjectRepository;
    private final SharePrivateRepository sharePrivateRepository;
    private final SubjectDocumentRepository subjectDocumentRepository;
    private final DocumentRepository documentRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;
    private final ModelMapperUtils modelMapperUtils;
    private final GoogleCloudStorageService googleCloudStorageService;
    private final GoogleCloudTranslateService translateService;
    private final ApplicationEventPublisher publisher;

    @Autowired
    public SubjectServiceImpl(
            SubjectRepository subjectRepository,
            UserRepository userRepository,
            TeacherRepository teacherRepository,
            ModelMapperUtils modelMapperUtils,
            GoogleCloudStorageService googleCloudStorageService,
            DocumentRepository documentRepository,
            SubjectDocumentRepository subjectDocumentRepository,
            GoogleCloudTranslateService translateService,
            SharePrivateRepository sharePrivateRepository,
            FavoriteSubjectRepository favoriteSubjectRepository,
            CommentSubjectDocumentRepository commentSubjectDocumentRepository,
            ApplicationEventPublisher publisher
    ) {
        this.subjectRepository = subjectRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
        this.googleCloudStorageService = googleCloudStorageService;
        this.documentRepository = documentRepository;
        this.subjectDocumentRepository = subjectDocumentRepository;
        this.translateService = translateService;
        this.sharePrivateRepository = sharePrivateRepository;
        this.favoriteSubjectRepository = favoriteSubjectRepository;
        this.commentSubjectDocumentRepository = commentSubjectDocumentRepository;
        this.publisher = publisher;
    }

    @Override
    public List<SubjectDto> getAllSubjectsForFilter() {
        return subjectRepository.findAllSubjectName();
    }

    @Override
    public List<String> findAllSemesterForFilter() {
        return subjectDocumentRepository.findAllSemester();
    }

    @Override
    public List<SubjectDocumentType> findAllSubjectDocumentType() {
        return subjectDocumentRepository.findAllSubjectDocumentType();
    }

    @Override
    public SubjectDocument getStSubjectDetail(Long subjectId) {
        return subjectDocumentRepository.findById(subjectId).orElse(null);
    }


    @Override
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }

    @Override
    public Subject createSubject(SubjectModel subjectModel) {
        Teacher teacher = teacherRepository.findById(subjectModel.getTeacherId()).orElse(null);
        if (teacher == null) return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Subject subject = modelMapperUtils.mapAllProperties(subjectModel, Subject.class);
        subject.setOwner(user);
        subject.getTeachers().add(teacher);
        subjectRepository.save(subject);
        subject.setOwner(null);
        subject.setTeachers(null);
        return subject;
    }

    @Override
    public boolean updateSubject(SubjectModel subjectModel) {
        Subject subject = subjectRepository.findById(subjectModel.getId()).orElse(null);
        if (subject == null || !subject.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        subject.setName(subjectModel.getName());
        subject.setDescription(subjectModel.getDescription());
        subjectRepository.save(subject);
        return true;
    }

    @Override
    public boolean deleteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null)
            return false;
        subjectRepository.deleteById(subjectId);
        return true;
    }

    @Override
    public List<Subject> getAllSubjectsCreateByUser() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return subjectRepository.findAllByOwner(user);
    }

    @Override
    public List<Subject> findSubjectByKeywordAndTeachers(String keyword, Teacher teacher) {
        List<Subject> subjects = subjectRepository.findByKeywordAndTeachers(keyword, teacher);
        log.info("Number of subjectLists : {}", subjects.size());
        return subjects;
    }

    @Override
    public SubjectDocument saveDocumentForSubject(SubjectDocumentModel subjectDocumentModel) {
        int length = subjectDocumentModel.getDocuments().length;
        if (length == 0 && subjectDocumentModel.getContentText() == null)
            return null;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (length > 0) {
            try {
                String path = googleCloudStorageService.uploadDocumentsToGCP(subjectDocumentModel.getDocuments(), user.getRootPath());
                Document document = modelMapperUtils.mapAllProperties(subjectDocumentModel, Document.class);
                document.setType(length > 1 ? DocumentType.FOLDER : DocumentType.getDocumentTypeFromExtension(subjectDocumentModel.getDocuments()[0].getOriginalFilename().substring(subjectDocumentModel.getDocuments()[0].getOriginalFilename().lastIndexOf("."))));
                document.setContentType(length > 1 ? "multipart/form-data" : subjectDocumentModel.getDocuments()[0].getContentType());
                document.setName(subjectDocumentModel.getDocuments()[0].getOriginalFilename());
                document.setPath(path);

                Document documentEntity = documentRepository.save(document);
                Subject subject = subjectRepository.findById(subjectDocumentModel.getSubjectId()).orElse(null);
                SubjectDocument subjectDocument = modelMapperUtils.mapAllProperties(subjectDocumentModel, SubjectDocument.class);
                subjectDocument.setDocument(documentEntity);
                subjectDocument.setOwner(user);
                subjectDocument.setSubject(subject);
                subjectDocument.setDescriptionEn(translateService.translateText(subjectDocument.getDescription(), TargetLanguageType.ENGLISH).get(0));
                subjectDocument = subjectDocumentRepository.save(subjectDocument);
                publisher.publishEvent(new NotifyEvent(NotificationType.NEW_SUBJECT_DOCUMENT, subjectDocument));
                return subjectDocument;
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Object shareDocument(ShareSubjectDocumentModel shareSubjectDocumentModel) {
        Document document = documentRepository.findById(shareSubjectDocumentModel.getDocumentId()).orElse(null);
        if (document == null) return null;
        SubjectDocument subjectDocument = subjectDocumentRepository.findByDocument(document);
        if (subjectDocument == null || !subjectDocument.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return null;
        switch (shareSubjectDocumentModel.getShareType()) {
            case PUBLIC -> {
                if (!subjectDocument.isPublic()) return null;
                subjectDocument.setPublic(true);
                return subjectDocumentRepository.save(subjectDocument);
            }
            case PRIVATE -> {
                if (shareSubjectDocumentModel.getUserIds() == null || shareSubjectDocumentModel.getUserIds().isEmpty())
                    return null;
                shareSubjectDocumentModel.getUserIds().forEach(userId -> {
                    User user = userRepository.findById(userId).orElse(null);
                    if (user != null) {
                        SharePrivate sharePrivate = new SharePrivate();
                        sharePrivate.setDocument(document);
                        sharePrivate.setUser(user);
                        sharePrivateRepository.save(sharePrivate);
                        publisher.publishEvent(new NotifyEvent(NotificationType.SHARE_DOCUMENT_PRIVATE, sharePrivate));
                    }
                });
                return "Ok";
            }
            case ANYONE_HAS_LINK -> {
                String token = UUID.randomUUID().toString();
                ShareByLink shareByLink = new ShareByLink();
                shareByLink.setDocument(document);
                shareByLink.setToken(token);
                googleCloudStorageService.setAclForAccessBlob(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER), subjectDocument.getOwner().getRootPath() + "documents/" + document.getPath() + document.getName());
                return "https://storage.googleapis.com/" + "hust-document-file" + "/" + subjectDocument.getOwner().getRootPath() + "documents/" + document.getPath() + document.getName();
            }

        }
        return false;
    }

    @Override
    public boolean favoriteSubject(FavoriteSubjectModel favoriteSubjectModel) {
        Subject subject = subjectRepository.findById(favoriteSubjectModel.getSubjectId()).orElse(null);
        if (subject == null) return false;
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (user == null) return false;
        FavoriteSubject favoriteSubject = new FavoriteSubject();
        favoriteSubject.setSubject(subject);
        favoriteSubject.setUser(user);
        favoriteSubject.setNotificationType(favoriteSubjectModel.getNotificationType());
        favoriteSubjectRepository.save(favoriteSubject);
        return true;
    }

    @Override
    public boolean unFavoriteSubject(Long subjectId) {
        FavoriteSubject favoriteSubject = favoriteSubjectRepository.findById(subjectId).orElse(null);
        if (favoriteSubject == null || !favoriteSubject.getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        favoriteSubjectRepository.deleteById(subjectId);
        return true;
    }

    @Override
    public boolean updateFavoriteSubject(FavoriteSubjectModel favoriteSubjectModel) {
        FavoriteSubject favoriteSubject = favoriteSubjectRepository.findById(favoriteSubjectModel.getId()).orElse(null);
        if (favoriteSubject == null || !favoriteSubject.getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        if (favoriteSubject.getNotificationType() != favoriteSubjectModel.getNotificationType()) {
            favoriteSubject.setNotificationType(favoriteSubjectModel.getNotificationType());
            favoriteSubjectRepository.save(favoriteSubject);
        }
        return true;
    }

    @Override
    public CommentSubjectDocument createCommentForSubjectDocument(CommentSubjectDocumentModel commentSubjectDocumentModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(commentSubjectDocumentModel.getSubjectDocumentId()).orElse(null);
        if (subjectDocument == null) return null;
        SharePrivate sharePrivate = sharePrivateRepository.findByDocumentAndUser(subjectDocument.getDocument(), user);
        if (!subjectDocument.isPublic() && sharePrivate == null) return null;
        CommentSubjectDocument comment = modelMapperUtils.mapAllProperties(commentSubjectDocumentModel, CommentSubjectDocument.class);
        comment.setSubjectDocument(subjectDocument);
        comment.setOwner(user);
        comment = commentSubjectDocumentRepository.save(comment);
        publisher.publishEvent(new NotifyEvent(NotificationType.NEW_COMMENT_SUBJECT_DOCUMENT, comment));
        return comment;
    }

    @Override
    public boolean updateCommentForSubjectDocument(CommentSubjectDocumentModel commentSubjectDocumentModel) {
        if (commentSubjectDocumentModel.getId() == null) return false;
        CommentSubjectDocument comment = commentSubjectDocumentRepository.findById(commentSubjectDocumentModel.getId()).orElse(null);
        if (comment == null || !comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        comment.setComment(commentSubjectDocumentModel.getComment());
        commentSubjectDocumentRepository.save(comment);
        publisher.publishEvent(new NotifyEvent(NotificationType.EDIT_COMMENT_SUBJECT_DOCUMENT, comment));
        return true;
    }

    @Override
    public boolean deleteCommentForSubjectDocument(@NotNull Long id) {
        CommentSubjectDocument comment = commentSubjectDocumentRepository.findById(id).orElse(null);
        if (comment == null || !comment.getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        commentSubjectDocumentRepository.deleteById(id);
        publisher.publishEvent(new NotifyEvent(NotificationType.DELETE_COMMENT_SUBJECT_DOCUMENT, comment));
        return true;
    }

    @Override
    public boolean hiddenCommentForSubjectDocument(Long id) {
        CommentSubjectDocument comment = commentSubjectDocumentRepository.findById(id).orElse(null);
        if (comment == null || !comment.getSubjectDocument().getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        comment.setHidden(true);
        commentSubjectDocumentRepository.save(comment);
        publisher.publishEvent(new NotifyEvent(NotificationType.HIDDEN_COMMENT_SUBJECT_DOCUMENT, comment));
        return true;
    }

    @Override
    public boolean activeCommentForSubjectDocument(Long id) {
        CommentSubjectDocument comment = commentSubjectDocumentRepository.findById(id).orElse(null);
        if (comment == null || !comment.getSubjectDocument().getOwner().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()))
            return false;
        comment.setHidden(false);
        commentSubjectDocumentRepository.save(comment);
        publisher.publishEvent(new NotifyEvent(NotificationType.ACTIVE_COMMENT_SUBJECT_DOCUMENT, comment));
        return true;
    }

    @Override
    public List<FavoriteSubject> getFavoriteSubjects() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return favoriteSubjectRepository.findByUser(user);
    }

    @Override
    public boolean updateDocumentForSubject(SubjectDocumentModel subjectDocumentModel) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentModel.getId()).orElse(null);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (subjectDocument == null || !subjectDocument.getOwner().getEmail().equals(user.getEmail())) return false;
        subjectDocument.getDocument().setName(subjectDocumentModel.getDocuments()[0].getOriginalFilename());
        try {
            googleCloudStorageService.updateDocumentByRootPath(user.getRootPath() + "documents/" + subjectDocument.getDocument().getPath(), subjectDocumentModel.getDocuments()[0]);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteDocumentForSubject(SubjectDocumentModel subjectDocumentModel) {
        SubjectDocument subjectDocument = subjectDocumentRepository.findById(subjectDocumentModel.getId()).orElse(null);
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (subjectDocument == null || !subjectDocument.getOwner().getEmail().equals(user.getEmail())) return false;
        String path =user.getRootPath() + "documents/" + subjectDocument.getDocument().getPath();
        boolean status = googleCloudStorageService.deleteAllDocumentByRootPath(path);
        log.info(user.getRootPath() + "documents/" + subjectDocument.getDocument().getPath());
        if (!status) return false;
        subjectDocumentRepository.delete(subjectDocument);
        documentRepository.delete(subjectDocument.getDocument());
        return true;
    }

    @Override
    public List<User> getAllUserShareWithMe() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return sharePrivateRepository.findByUser(user);
    }

    @Override
    public List<SubjectDto> getAllSubjects() {
        return subjectRepository.findAllSubjects();
    }
}
