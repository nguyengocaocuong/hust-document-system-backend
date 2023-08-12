package com.hust.edu.vn.documentsystem.service.impl;

import com.google.cloud.storage.Acl;
import com.hust.edu.vn.documentsystem.common.type.RoleType;
import com.hust.edu.vn.documentsystem.data.dto.ReviewSubjectDto;
import com.hust.edu.vn.documentsystem.data.dto.ReviewTeacherDto;
import com.hust.edu.vn.documentsystem.data.model.PasswordModel;
import com.hust.edu.vn.documentsystem.data.model.UserModel;
import com.hust.edu.vn.documentsystem.entity.*;
import com.hust.edu.vn.documentsystem.event.RegistrationCompleteEvent;
import com.hust.edu.vn.documentsystem.repository.*;
import com.hust.edu.vn.documentsystem.service.EmailService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.UserService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final ReviewSubjectRepository reviewSubjectRepository;
    private final ReviewTeacherRepository reviewTeacherRepository;
    private final SubjectDocumentRepository subjectDocumentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final VerifyAccountRepository verifyAccountRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final ApplicationEventPublisher publisher;

    private final ModelMapperUtils modelMapperUtils;

    private final GoogleCloudStorageService googleCloudStorageService;

    private final EmailService emailService;


    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            VerifyAccountRepository verifyAccountRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            ApplicationEventPublisher publisher, ModelMapperUtils modelMapperUtils,
            GoogleCloudStorageService googleCloudStorageService, EmailService emailService,
            SubjectDocumentRepository subjectDocumentRepository,
            ReviewTeacherRepository reviewTeacherRepository,
            ReviewSubjectRepository reviewSubjectRepository) {
        this.googleCloudStorageService = googleCloudStorageService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verifyAccountRepository = verifyAccountRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.publisher = publisher;
        this.emailService = emailService;
        this.subjectDocumentRepository = subjectDocumentRepository;
        this.reviewTeacherRepository = reviewTeacherRepository;
        this.reviewSubjectRepository = reviewSubjectRepository;
    }

    @Override
    public boolean registerUser(UserModel userModel, String applicationUrl) {
        if (userRepository.existsByEmail(userModel.getEmail()))
            return false;
        User user = modelMapperUtils.mapAllProperties(userModel, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleType(RoleType.USER);
        userRepository.save(user);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl));
        return true;
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerifyAccount verifyAccount = new VerifyAccount(user, token);
        verifyAccountRepository.save(verifyAccount);
    }

    @Override
    public boolean validateVerificationToken(String token) {
        VerifyAccount verifyAccount = verifyAccountRepository.findByToken(token);
        if (verifyAccount == null) {
            return false;
        }

        User user = verifyAccount.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verifyAccount.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            return false;
        }
        user.setEnable(true);
        userRepository.save(user);
        verifyAccountRepository.delete(verifyAccount);
        return true;
    }


    @Override
    public boolean chainPasswordForUserByEmailAndPassword(PasswordModel passwordModel) {
        User user = userRepository.findByEmail(passwordModel.getEmail());
        if (user == null || !passwordEncoder.matches(passwordModel.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public User checkAccountForLogin(UserModel userModel) {
        User user = userRepository.findByEmail(userModel.getEmail());
        if (user == null || !passwordEncoder.matches(userModel.getPassword(), user.getPassword()))
            return null;
        return user;
    }

    @Override
    public boolean generateNewVerificationToken(String oldToken, String email, String applicationUrl) {
        VerifyAccount verifyAccount = null;
        if (oldToken != null) {
            verifyAccount = verifyAccountRepository.findByToken(oldToken);
        } else if (email != null) {
            User user = userRepository.findByEmail(email);
            if (user == null) return false;
            verifyAccount = verifyAccountRepository.findByUser(user);
        }
        if (verifyAccount == null) return false;
        verifyAccount.setToken(UUID.randomUUID().toString());
        verifyAccountRepository.save(verifyAccount);
        return resendVerificationAccountTokenMail(applicationUrl, verifyAccount);
    }

    @Override
    public User getProfile() {
        return userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public User updateProfile(UserModel userModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setAvatar(userModel.getAvatar());
        if (userModel.getAvatarFile() != null) {
            try {
                List<String> urls = googleCloudStorageService.createThumbnailAndUploadDocumentToGCP(userModel.getAvatarFile(), List.of(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)));
                user.setAvatar(urls.get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setPhoneNumber(userModel.getPhoneNumber());
        user.setAddress(userModel.getAddress());
        user.setFacebookUrl(userModel.getFacebookUrl());
        user.setTwitterUrl(userModel.getTwitterUrl());
        user.setInstagramUrl(userModel.getInstagramUrl());
//        user.setDob(userModel.getDob());


        return userRepository.save(user);
    }

    @Override
    public List<SubjectDocument> getAllSubjectDocumentTrash() {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        return subjectDocumentRepository.findAllByIsDeleteAndOwner(true, user);
    }

    @Override
    public List<Object[]> getUserForDashboard(Date sevenDaysAgo) {
        return userRepository.getUserForDashboard(sevenDaysAgo);
    }

    @Override
    public List<User> getAllNewUser() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysAgo = calendar.getTime();
        return userRepository.getAllNewUser(sevenDaysAgo);
    }

    @Override
    public List<User> getAllForAdminUser() {
        return userRepository.findAll();
    }

    @Override
    public Object[] getProfileForUser(Long userId) {
        return userRepository.getProfileUser(userId);
    }

    @Override
    public Object[] getSubjectDocumentForProfile(Long userId) {
        return userRepository.getSubjectDocumentForProfile(userId);
    }

    @Override
    public boolean createUser(UserModel userModel) {
        if (userRepository.existsByEmail(userModel.getEmail()) || !userModel.getEmail().endsWith("@sis.hust.edu.vn") || userModel.getPassword().length() < 8)
            return false;
        User user = new User();
        if (userModel.getAvatarFile() != null) {
            try {
                List<String> urls = googleCloudStorageService.createThumbnailAndUploadDocumentToGCP(userModel.getAvatarFile(), List.of(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)));
                user.setAvatar(urls.get(0));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setLastName(userModel.getLastName());
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setPhoneNumber(userModel.getPhoneNumber());
        user.setAddress(userModel.getAddress());
        user.setFacebookUrl(userModel.getFacebookUrl());
        user.setTwitterUrl(userModel.getTwitterUrl());
        user.setInstagramUrl(userModel.getInstagramUrl());
        user.setEnable(true);
        user.setRoleType(userModel.getRoleType());
//        user.setDob(userModel.getDob());
        userRepository.save(user);
        return true;
    }

    @Override
    public List<SubjectDocument> getObjectForRecommend(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        return userRepository.getSubjectDocumentForRecommend(user.getId(), pageRequest);
    }

    @Override
    public String updateAvatar(MultipartFile avatar) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        try {
            if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
                String path = user.getAvatar().split(System.getenv("BUCKET_NAME") + "/")[1];
                googleCloudStorageService.deleteBlobByPath(path);
            }
            String url = googleCloudStorageService.uploadAvatarToGCP(avatar);
            user.setAvatar(url);
            userRepository.save(user);
            return url;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public User updateUserInfo(UserModel userModel) {
        log.info(userModel.toString());
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setInstagramUrl(userModel.getInstagramUrl());
        user.setFacebookUrl(userModel.getFacebookUrl());
        user.setTwitterUrl(userModel.getTwitterUrl());
        user.setAddress(userModel.getAddress());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPhoneNumber(userModel.getPhoneNumber());

        return userRepository.save(user);
    }

    @Override
    public User updateAccountInfo(UserModel userModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (passwordEncoder.matches(userModel.getOldPassword(), user.getPassword())) {
            if (userModel.getNewPassword() != null && userModel.getNewPassword().length() > 7)
                user.setPassword(passwordEncoder.encode(userModel.getNewPassword()));
            user.setUsername(userModel.getUsername());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public User getUserProfile(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public Map<String, Object> getAllWroteByUserId(Long userId) {
        List<ReviewTeacher> reviewTeachers = reviewTeacherRepository.findAllWroteByUseId(userId);
        List<ReviewSubject> reviewSubjects = reviewSubjectRepository.findAllWroteByUserId(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("reviewTeachers", reviewTeachers.stream().map(review -> modelMapperUtils.mapAllProperties(review, ReviewTeacherDto.class)));
        result.put("reviewSubjects", reviewSubjects.stream().map(review -> modelMapperUtils.mapAllProperties(review, ReviewSubjectDto.class)));
        return result;
    }

    private boolean resendVerificationAccountTokenMail(String applicationUrl, VerifyAccount verifyAccount) {
        String url = applicationUrl
                + "/api/v1/auth/verifyRegistrationToken?token="
                + verifyAccount.getToken();
        return emailService.sendSimpleMessage(verifyAccount.getUser(), "Kích hoạt tài khoản", url);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean createPasswordResetTokenForUser(String email) {
        User user = findUserByEmail(email);
        if (user == null) return false;
        PasswordResetToken oldPasswordResetToken = passwordResetTokenRepository.findByUser(user);
        String token = UUID.randomUUID().toString() +UUID.randomUUID() +UUID.randomUUID();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        if (oldPasswordResetToken != null){
            oldPasswordResetToken.setToken(token);
            oldPasswordResetToken.setExpirationTime(tomorrow);
            passwordResetTokenRepository.save(oldPasswordResetToken);
        }
       else {
            PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
            passwordResetToken.setExpirationTime(tomorrow);
            passwordResetTokenRepository.save(passwordResetToken);
        }
        return sendPasswordResetTokenMail(user, token);
    }

    private boolean sendPasswordResetTokenMail(User user, String token) {
        String url = System.getenv("FRONTEND_URL") + "/reset-password/" + token;
        return emailService.sendSimpleMessageForResetPassword(user, "Đặt lại mật khẩu", url);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public boolean validatePasswordResetToken(String token, PasswordModel passwordModel) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        Calendar calendar = Calendar.getInstance();
        User user = passwordResetToken.getUser();
        log.info(user.toString());
        log.info(passwordModel.toString());
        log.info("{}",!user.getEmail().equals(passwordModel.getEmail().trim()));
        if (passwordResetToken == null || !user.getEmail().equals(passwordModel.getEmail().trim()) || (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
        return true;
    }
}
