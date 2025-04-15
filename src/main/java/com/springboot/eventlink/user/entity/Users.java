package com.springboot.eventlink.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "MEMBERS") // 테이블 이름 변경
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_NUM_PK")
    private Long id;

    @Column(name = "MEMBER_ID")
    private String username;

    @Column(name = "MEMBER_NM")
    private String name;

    @Column(name = "MEMBER_GENDER")
    private String gender;

    @Column(name = "MEMBER_EMAIL")
    private String email;

    @Column(name = "MEMBER_COUNTRY")
    private String country;

    @Column(name = "MEMBER_ROLE")
    private String role;

    @Column(name = "SOCIAL_TYPE")
    private String socialType; // ex: google, naver
}
