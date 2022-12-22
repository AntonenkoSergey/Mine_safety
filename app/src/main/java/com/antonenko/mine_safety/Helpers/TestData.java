package com.antonenko.mine_safety.Helpers;

import java.io.Serializable;
import java.util.ArrayList;

public class TestData implements Serializable{
    private ArrayList<Question> Questions;

    public TestData() {

    }


    public TestData(ArrayList<Question> questions) {
        Questions = questions;
    }

    public ArrayList<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        Questions = questions;
    }
}
