package com.gustavosouza.votacao.security;

public enum UserRoles {

    MANAGER("manager"),

    ADMIN("admin"),

    USER("user");

    private String role;

    UserRoles(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
