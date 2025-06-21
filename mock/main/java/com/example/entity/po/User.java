package com.example.entity.po;

// 用户实体类
public class User {
    private String imsi;
    private int gender; // 0:女, 1:男
    private int age;

    public User(String imsi, int gender, int age) {
        this.imsi = imsi;
        this.gender = gender;
        this.age = age;
    }

    // Getters
    public String getImsi() { return imsi; }
    public int getGender() { return gender; }
    public int getAge() { return age; }
}
