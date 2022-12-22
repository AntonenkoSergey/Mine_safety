package com.antonenko.mine_safety.Helpers;

import java.io.Serializable;

public class User implements Serializable {
    private String PIB;
    private String time;
    private int right_answer;
    private int wrong_answer;
    private int total_questions;

    public User() {
    }

    public User(String PIB, String time, int right_answer, int wrong_answer, int total_questions) {
        this.PIB = PIB;
        this.time = time;
        this.right_answer = right_answer;
        this.wrong_answer = wrong_answer;
        this.total_questions = total_questions;
    }

    public int getTotal_questions() {
        return total_questions;
    }

    public void setTotal_questions(int total_questions) {
        this.total_questions = total_questions;
    }

    public String getPIB() {
        return PIB;
    }

    public void setPIB(String PIB) {
        this.PIB = PIB;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(int right_answer) {
        this.right_answer = right_answer;
    }

    public int getWrong_answer() {
        return wrong_answer;
    }

    public void setWrong_answer(int wrong_answer) {
        this.wrong_answer = wrong_answer;
    }
}
