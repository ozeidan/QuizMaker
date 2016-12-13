package com.apps.omar.quiz.Backend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by omar on 01.12.16.
 */

public class Quiz implements Serializable {
    private String quizName = null;
    private String quizDescription = null;
    private ArrayList<Question> questions = new ArrayList<>();
    private Random random = new Random();
    private long id = -1;

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question)
    {
        questions.add(question);

    }

    public void removeQuestion(int index) {
        questions.remove(index);
    }

    public String getQuizDescription() {
        return quizDescription;
    }

    public void setQuizDescription(String quizDescription) {
        this.quizDescription = quizDescription;
    }

    public Question playRandomQuestion() {
        Question question = questions.get(random.nextInt(questions.size()));
        questions.remove(question);
        return question;
    }

    public boolean hasQuestion() {
        return !questions.isEmpty();
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
