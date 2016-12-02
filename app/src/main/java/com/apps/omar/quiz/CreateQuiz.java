package com.apps.omar.quiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.Quiz;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CreateQuiz extends AppCompatActivity {

    private Quiz quiz = new Quiz();
    private ListView questionList;
    private ArrayList<String> questionNames = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        questionList = (ListView) findViewById(R.id.question_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, questionNames);
        questionList.setAdapter(adapter);
    }

    public void addQuestion(View view)
    {
        Intent intent = new Intent(this, CreateQuestion.class);
        startActivityForResult(intent, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                if (resultCode == Activity.RESULT_OK) {
                    Question question = (Question) data.getSerializableExtra("question");
                    quiz.addQuestion(question);
                    adapter.add(question.getQuestion());
                }
                break;
            }
        }
    }
}
