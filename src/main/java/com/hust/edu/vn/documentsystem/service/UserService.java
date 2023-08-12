package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.PasswordModel;
import com.hust.edu.vn.documentsystem.data.model.UserModel;
import com.hust.edu.vn.documentsystem.entity.SubjectDocument;
import com.hust.edu.vn.documentsystem.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserService {
    boolean registerUser(UserModel userModel, String applicationUrl);

    void saveVerificationTokenForUser(User user, String token);


    boolean validateVerificationToken(String token);

    User findUserByEmail(String email);

    boolean createPasswordResetTokenForUser(String email);

    List<User> getAllUser();

    boolean validatePasswordResetToken(String token, PasswordModel passwordModel);

    boolean chainPasswordForUserByEmailAndPassword(PasswordModel passwordModel);

    User checkAccountForLogin(UserModel userModel);

    boolean generateNewVerificationToken(String oldToken, String email, String applicationUrl);

    User getProfile();

    User updateProfile(UserModel userModel);


    List<SubjectDocument> getAllSubjectDocumentTrash();

    List<Object[]> getUserForDashboard(Date sevenDaysAgo);

    List<User> getAllNewUser();

    List<User> getAllForAdminUser();

    Object[] getProfileForUser(Long userId);

    Object[] getSubjectDocumentForProfile(Long userId);

    boolean createUser(UserModel userModel);

    List<SubjectDocument> getObjectForRecommend(int page, int size);

    String updateAvatar(MultipartFile avatar);

    User updateUserInfo(UserModel userModel);

    User updateAccountInfo(UserModel userModel);

    User getUserProfile(Long userId);

    Map<String, Object> getAllWroteByUserId(Long userId);
}
