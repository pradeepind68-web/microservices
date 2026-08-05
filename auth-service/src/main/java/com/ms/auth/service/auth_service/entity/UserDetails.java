package com.ms.auth.service.auth_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user_details")
public class UserDetails {

    @Id
    @GeneratedValue
    private Integer userId;
    private String name;
    private String username;
    private String password;
    private String email;

    public UserDetails(){}

    private UserDetails(UserBuilder userBuilder){
        this.userId=userBuilder.userId;
        this.name=userBuilder.name;
        this.email=userBuilder.email;
        this.username=userBuilder.username;
        this.password=userBuilder.password;
    }

    public static UserBuilder builder(){
        return new UserBuilder();
    }
    public Integer getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public static class UserBuilder{
        private Integer userId;
        private String name;
        private String username;
        private String password;
        private String email;

        public UserBuilder userId(Integer userId){
            this.userId=userId;
            return this;
        }
        public UserBuilder username(String username){
            this.username=username;
            return this;
        }
        public UserBuilder password(String password){
            this.password=password;
            return this;
        }
        public UserBuilder name(String name){
            this.name=name;
            return this;
        }

        public UserBuilder email(String email){
            this.email=email;
            return this;
        }

        public UserDetails build(){
            if(this.username==null || this.username.trim().isEmpty()){
                throw new IllegalArgumentException("UserName can not be null or empty");
            }
            return new UserDetails(this);
        }
    }
}