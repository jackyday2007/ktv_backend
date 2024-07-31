package com.ispan.ktv.util;

import com.ispan.ktv.bean.Members;

public class LoginResponse {
    private Members member;
    private String token;

    public LoginResponse(Members member, String token) {
        this.member = member;
        this.token = token;
    }

    // Getters 和 Setters
    public Members getMember() {
        return member;
    }

    public void setMember(Members member) {
        this.member = member;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


