package com.example.celebrityquiz.firebaseAccess;

public class Rank {
    String username;
    String totalQuizNum;
    String elapsedTime;

    Rank(String username, String totalQuizNum, String elapsedTime){
        this.username = username;
        this.totalQuizNum = totalQuizNum;
        this.elapsedTime = elapsedTime;
    }

    public String getUsername() {
        return username;
    }

    public String getTotalQuizNum() {
        return totalQuizNum;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTotalQuizNum(String totalQuizNum) {
        this.totalQuizNum = totalQuizNum;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
}
