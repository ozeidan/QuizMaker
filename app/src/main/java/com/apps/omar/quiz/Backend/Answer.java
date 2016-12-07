package com.apps.omar.quiz.Backend;

import java.io.Serializable;

/**
 * Created by omar on 01.12.16.
 */

public class Answer implements Serializable{
    private String answer;
    private boolean correct;

    public Answer(String answer) {
        this.answer = answer;
        this.correct = false;
    }

    public Answer(String answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return correct;
    }
}
