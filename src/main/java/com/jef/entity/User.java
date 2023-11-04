package com.jef.entity;

import com.jef.constant.BasicConstant;

import java.io.Serializable;

/**
 * 用户信息
 * 以用户为核心
 * @author Jef
 * @create 2018/5/15 19:18
 */
public class User implements Serializable {
    private static final long serialVersionUID = -8514215816882785376L;
    // Redis缓存用户key
    public static final String OBJECT_KEY = "User";

    private Long id;

    private String name;

    private String password;

    private String phone;

    private int age;

    private Integer permission;

    private Integer admin;

    private Integer tabIndex;

    private String gender;

    private String descrription;

    public User() {

    }

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public Integer getAdmin() {
        return admin;
    }

    public void setAdmin(Integer admin) {
        this.admin = admin;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    @Override
    public String toString() {
        return "id=" + this.getId() + "；名称=" + this.getName() + "；年龄=" + this.getAge() + "；电话=" + this.getPhone();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescrription() {
        return descrription;
    }

    public void setDescrription(String descrription) {
        this.descrription = descrription;
    }

    public static User getBasicUser() {
        return new User(20, BasicConstant.USER_NAME);
    }
}
