package com.example.celebrityquiz.firebaseAccess;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Record implements Serializable, Comparable<Record> {
    String username;
    String totalQuizNum;
    String elapsedTime;

    public Record(String username, String totalQuizNum, String elapsedTime){
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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("totalQuizNum", totalQuizNum);
        result.put("elapsedTime", elapsedTime);

        return result;
    }

    @Override
    public int compareTo(Record record) {
        return Integer.parseInt(this.totalQuizNum) - Integer.parseInt(record.totalQuizNum);
    }

    public static class RecordQuizNumComparator implements Comparator<Record> {
        public int compare(Record record1, Record record2) {
            return Integer.parseInt(record2.getTotalQuizNum()) - Integer.parseInt(record1.getTotalQuizNum());
        }
    }
    public static class RecordElapsedTimeComparator implements Comparator<Record> {
        public int compare(Record record1, Record record2) {
            return Integer.parseInt(record1.getElapsedTime()) - Integer.parseInt(record2.getElapsedTime());
        }
    }
}

