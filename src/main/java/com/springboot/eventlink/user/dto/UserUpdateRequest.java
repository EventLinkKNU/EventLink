package com.springboot.eventlink.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String gender;   // "boy", "girl"
    private String country;
}