package com.apps.omar.quiz.Backend;

import java.util.ArrayList;

/**
 * Created by omar on 01.12.16.
 */

public class MultipleChoiceQuestion extends Question {
    private ArrayList<Answer> answers = new ArrayList<>();


    public MultipleChoiceQuestion(String question)
    {
        super(question);
    }

    public void addAnswer(Answer answer)
    {
        this.answers.add(answer);
    }




}
