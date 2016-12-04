package com.apps.omar.quiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apps.omar.quiz.Backend.Quiz;
import com.apps.omar.quiz.Backend.QuizParser;

import java.util.ArrayList;

public class QuizMenu extends AppCompatActivity {

    QuizAdapter adapter;
    ArrayList<Quiz> quizes;
    private int deleteQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_menu);

        quizes = QuizParser.loadQuizes(this);
        adapter = new QuizAdapter(this, quizes);

        ListView quizList = (ListView) findViewById(R.id.quiz_list);
        quizList.setAdapter(adapter);

        quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editQuiz(i);
            }
        });
    }

    public void createQuiz(View view) {
        Intent intent = new Intent(this, CreateQuiz.class);
        intent.putExtra("requestCode", 0);
        startActivityForResult(intent, 0);
    }

    public void editQuiz(int position)
    {
        deleteQuiz = position;
        Intent intent = new Intent(this, CreateQuiz.class);
        intent.putExtra("quiz", quizes.get(position));
        intent.putExtra("requestCode", 1);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case(0):
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    Quiz quiz = (Quiz) data.getSerializableExtra("quiz");
                    quizes.add(quiz);
                    adapter.notifyDataSetChanged();

                    QuizParser.saveQuiz(quiz, this);
                }
                break;
            }
            case(1):
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    QuizParser.deleteQuiz(this, quizes.get(deleteQuiz));
                    quizes.remove(deleteQuiz);
                    Quiz newQuiz = (Quiz) data.getSerializableExtra("quiz");
                    quizes.add(deleteQuiz, newQuiz);
                    adapter.notifyDataSetChanged();

                    QuizParser.saveQuiz(newQuiz, this);
                }
                break;
            }
        }
    }
}
