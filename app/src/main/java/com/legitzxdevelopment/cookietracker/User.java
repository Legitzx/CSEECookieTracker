package com.legitzxdevelopment.cookietracker;

public class User {
    private String username; // Username
    private String password; // Password
    private String name; // Name
    private String id; // ID Number
    private String grade; // Current Grade

    private int boost; // % Boost on Final Exam
    private int hwPass; // Amount of HW Passes
    private int quizPass; // Amount of Quiz Passes
    private int cookies; // Amount of Cookies

    private boolean isAdmin; // Checks if the user is an Admin

    public User() { }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getCookies() {
        return cookies;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getGrade() {
        return grade;
    }

    public int getBoost() {
        return boost;
    }

    public int getHwPass() {
        return hwPass;
    }

    public int getQuizPass() {
        return quizPass;
    }

    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCookies(int cookies) {
        this.cookies = cookies;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setBoost(int boost) {
        this.boost = boost;
    }

    public void setHwPass(int hwPass) {
        this.hwPass = hwPass;
    }

    public void setQuizPass(int quizPass) {
        this.quizPass = quizPass;
    }
}
