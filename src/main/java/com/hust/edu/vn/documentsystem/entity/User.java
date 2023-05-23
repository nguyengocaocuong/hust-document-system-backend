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

    @Column()
    private Date dob;

    @Column()
    private String avatar;

    @Column()
    private Date createdAt = new Date();

    @Column()
    private boolean isEnable = false;

    @Column()
    private String phoneNumber;

    @Column()
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column( unique = true)
    private String rootPath;
    private String address;
    private String username;
}
