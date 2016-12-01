package com.apps.omar.quiz.Backend;

import java.io.Serializable;

/**
 * Created by omar on 01.12.16.
 */

public class Question implements Serializable {
    private String question;

    public Question(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
