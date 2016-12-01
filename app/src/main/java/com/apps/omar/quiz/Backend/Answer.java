package com.apps.omar.quiz.Backend;

import java.io.Serializable;

/**
 * Created by omar on 01.12.16.
 */

public class Answer implements Serializable{
    public String answer;
    public boolean correctAnswer;

    public Answer(String answer, boolean correctAnswer) {
        this.answer = answer;
        this.correctAnswer = correctAnswer;
    }
}
