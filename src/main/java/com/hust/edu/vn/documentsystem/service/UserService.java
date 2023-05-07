package com.hust.edu.vn.documentsystem.service;

import com.hust.edu.vn.documentsystem.data.model.PasswordModel;
import com.hust.edu.vn.documentsystem.data.model.UserModel;
import com.hust.edu.vn.documentsystem.entity.User;

import java.util.List;

public interface UserService {
    boolean registerUser(UserModel userModel, String applicationUrl);

    void saveVerificationTokenForUser(User user, String token);


    boolean validateVerificationToken(String token);

    User findUserByEmail(String email);

    boolean createPasswordResetTokenForUser(String email, String applicationUrl);

    List<User> getAllUser();

    boolean validatePasswordResetToken(String token, PasswordModel passwordModel);

    boolean chainPasswordForUserByEmailAndPassword(PasswordModel passwordModel);

    User checkAccountForLogin(UserModel userModel);

    boolean generateNewVerificationToken(String oldToken, String email, String applicationUrl);

    User getProfile();

    boolean updateProfile(UserModel userModel);

    User getProfileUserById(Long userId);
}
