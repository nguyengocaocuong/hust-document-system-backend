package com.hust.edu.vn.documentsystem.entity;

import com.hust.edu.vn.documentsystem.common.type.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column( nullable = false)
    private String firstName;
    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;
    @Column( nullable = false)
    private String lastName;
    @Column( nullable = false, unique = true)
    private String email;
    @Column( nullable = false)
    private String password;
    private Date dob;
    private String avatar;
    private Date createdAt = new Date();
    private boolean isEnable = false;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    private String address;
    @Column(unique = true)
    private String username;
    @Column()
    private boolean isSetup = false;
}
