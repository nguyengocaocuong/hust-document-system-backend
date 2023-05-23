package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "PasswordResetToken")
@Setter
@Getter
@NoArgsConstructor
public class PasswordResetToken {
    private static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationTime;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_PasswordResetToken_User"))
    private User user;

    public PasswordResetToken(User user, String token){
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationDate();
    }
    public PasswordResetToken(String token){
        this.token = token;
        this.expirationTime = calculateExpirationDate();
    }

    private Date calculateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }

}
