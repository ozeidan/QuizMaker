package com.apps.omar.quiz.Backend;

import java.util.ArrayList;

/**
 * Created by omar on 01.12.16.
 * This class is messed up.
 * I wanted to build an array of answers based on the constructor variable yesOrNo and then pass it to the super constructor.
 * Because of Java wanting me to call the super function first in the constructor I had to play around it with the static method and a second constructor.
 */

public class YesNoQuestion extends Question {
    private boolean yesOrNo;

    public YesNoQuestion(String question, boolean yesOrNo)
    {
        this(question, makeAnswers(yesOrNo));
        this.yesOrNo = yesOrNo;
    }

    public YesNoQuestion(String question, ArrayList<Answer> answers)
    {
        super(question, answers);
    }

    private static ArrayList<Answer> makeAnswers(boolean yesOrNo) {
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Yes", yesOrNo));
        answers.add(new Answer("No", !yesOrNo));
        return answers;
    }

    public boolean isYesOrNo() {
        return yesOrNo;
    }
}
