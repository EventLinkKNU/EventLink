package com.springboot.eventlink.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NUM_PK")
    private Long id;

    @Column(name = "USER_ID")
    private String username;

    @Column(name = "USER_NM")
    private String name;

    @Column(name = "USER_GENDER")
    private String gender;

    @Column(name = "USER_EMAIL")
    private String email;

    @Column(name = "USER_COUNTRY")
    private String country;

    @Column(name = "USER_ROLE")
    private String role;

    @Column(name = "SOCIAL_TYPE")
    private String socialType; // ex: google, naver
}
