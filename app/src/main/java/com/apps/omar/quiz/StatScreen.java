package com.apps.omar.quiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.apps.omar.quiz.Backend.Score;

import java.util.Formatter;

public class StatScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_screen);

        TextView textView = (TextView) findViewById(R.id.stats_text);

        StringBuilder stringBuilder = new StringBuilder();
        Formatter formatter = new Formatter(stringBuilder);

        if (Score.getAttempts() > 0) {
            long answerAttempts = Score.getAnswerAttempts();
            long correctlyAnswered = Score.getCorrectAnswers();
            float percentage = (float) correctlyAnswered / answerAttempts * 100;


            formatter.format(getString(R.string.stats_text), Score.getAttempts(), answerAttempts, correctlyAnswered, (int) percentage);
            textView.setText(stringBuilder.toString());
        } else
            textView.setText(getString(R.string.no_stats_text));
    }
}
