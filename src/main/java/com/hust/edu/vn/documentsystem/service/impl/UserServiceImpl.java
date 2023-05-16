package com.hust.edu.vn.documentsystem.service.impl;

import com.hust.edu.vn.documentsystem.common.type.NotificationType;
import com.hust.edu.vn.documentsystem.common.type.RoleType;
import com.hust.edu.vn.documentsystem.data.model.PasswordModel;
import com.hust.edu.vn.documentsystem.data.model.UserModel;
import com.hust.edu.vn.documentsystem.entity.PasswordResetToken;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.entity.VerifyAccount;
import com.hust.edu.vn.documentsystem.event.NotifyEvent;
import com.hust.edu.vn.documentsystem.event.RegistrationCompleteEvent;
import com.hust.edu.vn.documentsystem.repository.PasswordResetTokenRepository;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.repository.VerifyAccountRepository;
import com.hust.edu.vn.documentsystem.service.EmailService;
import com.hust.edu.vn.documentsystem.service.GoogleCloudStorageService;
import com.hust.edu.vn.documentsystem.service.UserService;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final VerifyAccountRepository verifyAccountRepository;

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    private final ApplicationEventPublisher publisher;

    private final ModelMapperUtils modelMapperUtils;

    private final GoogleCloudStorageService googleCloudStorageService;

    private final EmailService emailService;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            VerifyAccountRepository verifyAccountRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            ApplicationEventPublisher publisher, ModelMapperUtils modelMapperUtils,
            GoogleCloudStorageService googleCloudStorageService, EmailService emailService) {
        this.googleCloudStorageService = googleCloudStorageService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verifyAccountRepository = verifyAccountRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.modelMapperUtils = modelMapperUtils;
        this.publisher = publisher;
        this.emailService = emailService;
    }

    @Override
    public boolean registerUser(UserModel userModel, String applicationUrl) {
        if (userRepository.existsByEmail(userModel.getEmail()))
            return false;
        String rootPath = UUID.randomUUID() + "/";
        User user = modelMapperUtils.mapAllProperties(userModel, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleType(RoleType.USER);
        user.setRootPath(rootPath);
        userRepository.save(user);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl));
//        publisher.publishEvent(new NotifyEvent(NotificationType.NEW_USER, user));
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
    public boolean updateProfile(UserModel userModel) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userModel.getId() == null || !userModel.getId().equals(user.getId()) || userModel.getEmail().equals(user.getEmail()))
            return false;
        User updateUser = modelMapperUtils.mapAllProperties(userModel, User.class);
        userRepository.save(updateUser);
        return true;
    }

    @Override
    public User getProfileUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    private boolean resendVerificationAccountTokenMail(String applicationUrl, VerifyAccount verifyAccount) {
        String url = applicationUrl
                + "/api/v1/auth/verifyRegistrationToken?token="
                + verifyAccount.getToken();
        return emailService.sendSimpleMessage(verifyAccount.getUser().getEmail(), "Kích hoạt tài khoản", url);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean createPasswordResetTokenForUser(String email, String applicationUrl) {
        User user = findUserByEmail(email);
        if (user == null) return false;
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepository.save(passwordResetToken);
        return sendPasswordResetTokenMail(user, applicationUrl, token);
    }

    private boolean sendPasswordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl
                + "/api/v1/auth/chainPasswordByToken?token="
                + token;
        return emailService.sendSimpleMessage(user.getEmail(), "Kích hoạt tài khoản", url);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public boolean validatePasswordResetToken(String token, PasswordModel passwordModel) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        Calendar calendar = Calendar.getInstance();
        if (passwordResetToken == null || (passwordResetToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            return false;
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
        return true;
    }
}
