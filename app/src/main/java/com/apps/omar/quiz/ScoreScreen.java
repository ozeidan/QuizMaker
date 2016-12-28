package com.apps.omar.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.apps.omar.quiz.Adapters.AnswerHistoryAdapter;
import com.apps.omar.quiz.Backend.Score;

public class ScoreScreen extends AppCompatActivity {
    GameState gameState;
    AnswerHistoryAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        Intent intent = this.getIntent();
        gameState = (GameState) intent.getExtras().get("gameState");


        adapter = new AnswerHistoryAdapter(this, gameState.getAnswerHistory());

        ((ListView) findViewById(R.id.answer_history_list)).setAdapter(adapter);

        TextView textView = (TextView) findViewById(R.id.score_text_view);
        textView.setText(gameState.getScoreString());


        // moved code to other function
        saveScore(gameState);
    }


    // saves the achieved score if it is a highscore
    private void saveScore(GameState gameState) {
        long id = gameState.getId();
        float score = gameState.getScoreFloat();

        if (score > Score.loadScore(id)) {
            Score.saveScore(id, score);
        }
    }

    public void nice(View view) {
        finish();
    }



}
