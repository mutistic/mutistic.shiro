package com.xfrj.user.model;

public class UserEntity {
    private Long id;

    private String username;

    private String password;

    private Integer authorType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getAuthorType() {
        return authorType;
    }

    public void setAuthorType(Integer authorType) {
        this.authorType = authorType;
    }
}