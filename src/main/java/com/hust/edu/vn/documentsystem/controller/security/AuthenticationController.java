package com.hust.edu.vn.documentsystem.controller.security;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import com.hust.edu.vn.documentsystem.data.model.AuthenticationModel;
import com.hust.edu.vn.documentsystem.data.model.PasswordModel;
import com.hust.edu.vn.documentsystem.data.model.UserModel;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.service.GoogleLanguageService;
import com.hust.edu.vn.documentsystem.service.ThumbnailService;
import com.hust.edu.vn.documentsystem.service.UserService;
import com.hust.edu.vn.documentsystem.service.impl.CustomUserDetailsService;
import com.hust.edu.vn.documentsystem.utils.JwtUtils;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final ModelMapperUtils modelMapperUtils;

    private final ThumbnailService thumbnailService;
    private final GoogleLanguageService googleLanguageService;


    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            UserService userService,
            JwtUtils jwtUtils,
            ModelMapperUtils modelMapperUtils, ThumbnailService thumbnailService, GoogleLanguageService googleLanguageService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.modelMapperUtils = modelMapperUtils;
        this.thumbnailService = thumbnailService;
        this.googleLanguageService = googleLanguageService;
    }

    @PostMapping("authenticate")
    public ResponseEntity<CustomResponse> authenticate(@ModelAttribute AuthenticationModel authenticationModel) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationModel.getEmail(), authenticationModel.getPassword())
        );
        final UserDetails userDetail = userDetailsService.loadUserByUsername(authenticationModel.getEmail());
        if (userDetail != null && userDetail.isEnabled()) {
            return CustomResponse.generateResponse(HttpStatus.OK, jwtUtils.generateToken(userDetail));
        }
        return CustomResponse.generateResponse(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("resendJwtToken")
    public ResponseEntity<CustomResponse> resendJwtToken(@RequestParam() String oldToken) {
        String email = jwtUtils.extractUserName(oldToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (userDetails == null) return CustomResponse.generateResponse(HttpStatus.NOT_FOUND);
        if (jwtUtils.isTokenValid(oldToken, userDetails)) {
            Map<String, String> map = new HashMap<>();
            map.put("token", jwtUtils.generateToken(userDetails));
            return CustomResponse.generateResponse(HttpStatus.OK, map);
        }
        return CustomResponse.generateResponse(HttpStatus.CONFLICT);
    }

    @PostMapping("register")
    public ResponseEntity<CustomResponse> registerUser(@ModelAttribute UserModel userModel, final HttpServletRequest httpServletRequest) {
        boolean status = userService.registerUser(userModel, applicationUrl(httpServletRequest));
        return CustomResponse.generateResponse(status ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @GetMapping("verificationAccountToken")
    public ResponseEntity<CustomResponse> verifyRegistration(@RequestParam("token") String token) {
        boolean status = userService.validateVerificationToken(token);
        return CustomResponse.generateResponse(status ? HttpStatus.OK : HttpStatus.CONFLICT);
    }

    @GetMapping("resendVerificationAccountToken")
    public ResponseEntity<CustomResponse> resendVerificationToken(@RequestParam(value = "token", required = false) String oldToken, @RequestParam(required = false, name = "email") String email, HttpServletRequest request) {
        boolean status = userService.generateNewVerificationToken(oldToken, email, applicationUrl(request));
        return CustomResponse.generateResponse(status ? HttpStatus.OK : HttpStatus.CONFLICT);
    }

    @GetMapping("resetPassword")
    public ResponseEntity<CustomResponse> resetPassword(@RequestParam String email, HttpServletRequest servletRequest) {
        boolean status = userService.createPasswordResetTokenForUser(email, applicationUrl(servletRequest));
        return CustomResponse.generateResponse(status ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @PostMapping("chainPasswordByToken")
    public ResponseEntity<CustomResponse> chainPasswordByToken(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {
        boolean status = userService.validatePasswordResetToken(token, passwordModel);
        return CustomResponse.generateResponse(status);
    }

    @PostMapping("chainPasswordByEmailAndPassword")
    public ResponseEntity<CustomResponse> chainPasswordByEmailAndPassword(@ModelAttribute PasswordModel passwordModel) {
        boolean status = userService.chainPasswordForUserByEmailAndPassword(passwordModel);
        return CustomResponse.generateResponse(status);
    }

    @PostMapping("login")
    public ResponseEntity<CustomResponse> login(@RequestBody UserModel userModel) {
        User user = userService.checkAccountForLogin(userModel);
        if (user == null || !user.isEnable()) {
            return CustomResponse.generateResponse(HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetail = userDetailsService.loadUserByUsername(userModel.getEmail());
        UserDto userDto = modelMapperUtils.mapAllProperties(user, UserDto.class);
        userDto.setToken(jwtUtils.generateToken(userDetail));
        return CustomResponse.generateResponse(HttpStatus.OK, userDto);
    }


    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "https://"
                + httpServletRequest.getServerName()
                + ":"
                + httpServletRequest.getServerPort()
                + httpServletRequest.getContextPath();
    }

    @GetMapping("babComment")
    public ResponseEntity<CustomResponse> detectBabComment(@RequestParam("comment") String comment) throws Exception {
        return CustomResponse.generateResponse(HttpStatus.OK,googleLanguageService.detectBabComment(comment));
    }

}
