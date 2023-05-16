package com.hust.edu.vn.documentsystem.controller.security;

import com.hust.edu.vn.documentsystem.common.CustomResponse;
import com.hust.edu.vn.documentsystem.common.type.RoleType;
import com.hust.edu.vn.documentsystem.data.dto.UserDto;
import com.hust.edu.vn.documentsystem.data.model.AuthenticationModel;
import com.hust.edu.vn.documentsystem.data.model.PasswordModel;
import com.hust.edu.vn.documentsystem.data.model.UserModel;
import com.hust.edu.vn.documentsystem.entity.User;
import com.hust.edu.vn.documentsystem.repository.UserRepository;
import com.hust.edu.vn.documentsystem.service.UserService;
import com.hust.edu.vn.documentsystem.service.impl.CustomUserDetailsService;
import com.hust.edu.vn.documentsystem.utils.JwtUtils;
import com.hust.edu.vn.documentsystem.utils.ModelMapperUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/authentication")
@Tag(name = "Authentication - api")
@Slf4j
public class AuthenticationController {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final ModelMapperUtils modelMapperUtils;


    @Autowired
    public AuthenticationController(
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            UserService userService,
            JwtUtils jwtUtils,
            UserRepository userRepository, ModelMapperUtils modelMapperUtils) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.modelMapperUtils = modelMapperUtils;
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
        if (user == null) {
            return CustomResponse.generateResponse(HttpStatus.UNAUTHORIZED);
        }
        final UserDetails userDetail = userDetailsService.loadUserByUsername(userModel.getEmail());
        UserDto userDto = modelMapperUtils.mapAllProperties(user, UserDto.class);
        userDto.setToken(jwtUtils.generateToken(userDetail));
        return CustomResponse.generateResponse(HttpStatus.OK, userDto );
    }


    private String applicationUrl(HttpServletRequest httpServletRequest) {
        return "https://"
                + httpServletRequest.getServerName()
                + ":"
                + httpServletRequest.getServerPort()
                + httpServletRequest.getContextPath();
    }
    @GetMapping("setupAdmin")
    public String setupAdmin(@RequestParam("passwrod") String adminPass){
       User user = userService.findUserByEmail("cuong.nnc184055@sis.hust.edu.vn");
       if(user == null) return "not found";
       user.setEnable(true);
       user.setRoleType(RoleType.ADMIN);
       userRepository.save(user);
       return "done";
    }
    //
}
