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
    float score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        Intent intent = this.getIntent();
        gameState = (GameState) intent.getExtras().get("gameState");

        score = (float) gameState.getCorrectlyAnswered() / gameState.getAnswered() * 100;

        adapter = new AnswerHistoryAdapter(this, gameState.getAnswerHistory());

        ((ListView) findViewById(R.id.answer_history_list)).setAdapter(adapter);

        TextView textView = (TextView) findViewById(R.id.score_text_view);
        textView.setText(Float.toString(score));

        Score.saveScore(gameState);
    }

    public void nice(View view) {
        finish();
    }



}
