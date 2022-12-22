package com.antonenko.mine_safety.Helpers;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    public String question;
    public ArrayList<String> answers;
    public int right_answer;

    public Question() {
    }

    public Question(String question, ArrayList<String> answers, int right_answer) {
        this.question = question;
        this.answers = answers;
        this.right_answer = right_answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public int getRight_answer() {
        return right_answer;
    }

    public void setRight_answer(int right_answer) {
        this.right_answer = right_answer;
    }
}
