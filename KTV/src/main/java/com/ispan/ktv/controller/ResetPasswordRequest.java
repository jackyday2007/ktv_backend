package com.ispan.ktv.controller;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    private String token;  // 重設密碼的 token
    private String newPassword;  // 新密碼

}

