package com.hust.edu.vn.documentsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "VerifyAccounts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VerifyAccount {
    private static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "user_id", unique = true, nullable = false,foreignKey = @ForeignKey(name = "fk_VerifyAccount_User"))
    private User user;

    private String token;

    private Date expirationTime;

    public VerifyAccount(User user, String token){
        this.user = user;
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
