package com.apps.omar.quiz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.omar.quiz.Backend.Answer;
import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.Quiz;

import java.util.ArrayList;

public class PlayQuiz extends AppCompatActivity {

    private GameState gs;
    private Button correctButton;
    private boolean quizDone = false;
    private Button nextButton;
    private LinearLayout answerButtonList;
    private ArrayList<Answer> answers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_quiz);


        Intent intent = getIntent();
        gs = (GameState) intent.getExtras().get("gameState");

        nextButton = (Button) findViewById(R.id.next_question_button);
        answerButtonList = (LinearLayout) findViewById(R.id.answer_button_list);

        playQuiz();
    }


    private void playQuiz() {
        TextView textView = (TextView) findViewById(R.id.play_quiz_question_name);
        Question currentQuestion = gs.getQuestion();
        textView.setText(currentQuestion.getQuestion());
        answerButtonList.removeAllViews();

        answers = currentQuestion.getAnswers();

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        for (Answer answer : answers) {
            Button answerButton = (Button) layoutInflater.inflate(R.layout.answer_button, null);
            answerButton.setText(answer.getAnswer());
            answerButtonList.addView(answerButton);
            setButtonOnClick(answerButton);

            if (answer == currentQuestion.getCorrectAnswer()) {
                correctButton = answerButton;
            }
        }
    }

    private void setButtonOnClick(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = answerButtonList.indexOfChild(view);
                if(gs.answerQuestion(answers.get(position)))
                {
                    win();
                }
                else
                {
                    lose((Button) view);
                }
            }
        });
    }


    private void win() {
        if (!quizDone) {
            correctButton.setBackgroundColor(Color.rgb(0, 230, 0));
            quizDone = true;
            updateButton();
        }
    }

    private void lose(Button button) {
        if (!quizDone) {
            correctButton.setBackgroundColor(Color.rgb(0, 230, 0));
            button.setBackgroundColor(Color.rgb(230, 0, 0));
            quizDone = true;
            updateButton();
        }
    }

    private void updateButton() {
        nextButton.setVisibility(View.VISIBLE);
        if (!gs.hasQuestion()) {
            nextButton.setText("Finish");
        }
    }

    public void nextQuestion(View view) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("gameState", gs);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}

