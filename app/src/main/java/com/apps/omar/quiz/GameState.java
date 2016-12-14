package com.apps.omar.quiz;

import android.util.Pair;

import com.apps.omar.quiz.Backend.Answer;
import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.Quiz;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by omar on 14.12.16.
 */

public class GameState implements Serializable{
    private int correctlyAnsweredCount = 0;

    private ArrayList<MyPair>  answerHistory = new ArrayList<>();

    private ArrayList<Question> unanswered;

    private Question currentQuestion;
    private boolean answering = false;

    private Random random = new Random();


    private Quiz quiz;

    public GameState(Quiz quiz)
    {
        this.quiz = quiz;
        unanswered = quiz.getQuestions();
    }

    public ArrayList<MyPair> getAnswerHistory() {
        return answerHistory;
    }

    public int getCorrectlyAnsweredCount() {
        return correctlyAnsweredCount;
    }

    public int getAnsweredCount()
    {
        return answerHistory.size();
    }

    public Question getQuestion()
    {
        if(answering)
        {
            throw new RuntimeException();
        }

        answering = true;


        int position = random.nextInt(unanswered.size());
        currentQuestion = unanswered.get(position);
        unanswered.remove(position);
        return currentQuestion;
    }

    public boolean answerQuestion(Answer answer)
    {
        if(!answering)
        {
            throw new RuntimeException();
        }
        answering = false;
        if (answer == currentQuestion.getCorrectAnswer())
        {
            correctlyAnsweredCount++;
            answerHistory.add(new MyPair(currentQuestion, true));
            return  true;
        }
        else
        {
            answerHistory.add(new MyPair(currentQuestion, false));
            return false;
        }
    }


    public boolean hasQuestion()
    {
        return !unanswered.isEmpty();
    }

    public ArrayList<Answer> getAnswers()
    {
        return currentQuestion.getAnswers();
    }

    //implemented own pair class because Pair is not serializable
    private class MyPair implements Serializable
    {
        public Question question;
        public boolean correct;

        public MyPair(Question question, boolean correct) {
            this.question = question;
            this.correct = correct;
        }
    }
}
