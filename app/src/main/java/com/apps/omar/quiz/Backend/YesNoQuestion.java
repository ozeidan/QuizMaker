package com.apps.omar.quiz.Backend;

/**
 * Created by omar on 01.12.16.
 */

public class YesNoQuestion extends Question{
    private boolean yesOrNo;

    public YesNoQuestion(String question, boolean yesOrNo)
    {
        this.setQuestion(question);
        this.yesOrNo = yesOrNo;
    }

    public YesNoQuestion()
    {

    }

    public boolean isYesOrNo() {
        return yesOrNo;
    }

    public void setYesOrNo(boolean yesOrNo) {
        this.yesOrNo = yesOrNo;
    }
}
