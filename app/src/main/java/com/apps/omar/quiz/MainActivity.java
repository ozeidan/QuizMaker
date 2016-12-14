package com.apps.omar.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.apps.omar.quiz.Backend.Score;

public class MainActivity extends AppCompatActivity {

    static {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Score.initialize(this);
    }

    public void createMenu(View view)
    {
        Intent intent = new Intent(this, QuizMenu.class);
        intent.putExtra("requestCode", 1);
        startActivity(intent);
    }

    public void playMenu(View view) {
        Intent intent = new Intent(this, QuizMenu.class);
        intent.putExtra("requestCode", 0);
        startActivity(intent);
    }

}
