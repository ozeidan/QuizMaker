package com.apps.omar.quiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.Quiz;
import com.apps.omar.quiz.Backend.QuizParser;

import java.util.ArrayList;

public class CreateQuiz extends AppCompatActivity {

    private Quiz quiz = new Quiz();
    private ListView questionList;
    QuestionAdapter adapter;
    private int deleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        questionList = (ListView) findViewById(R.id.question_list);
        adapter = new QuestionAdapter(this, quiz);
        questionList.setAdapter(adapter);

        questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deleteItem = i;
                editQuestion(quiz.getQuestions().get(i));
            }
        });

        Intent intent = getIntent();

        if((int)(intent.getExtras().get("requestCode")) == 1)
        {
            quiz = (Quiz) intent.getExtras().get("quiz");

            EditText quizName = (EditText) findViewById(R.id.quiz_name);
            quizName.setText(quiz.getQuizName());

            if(quiz.getQuizDescription() != null)
            {
                EditText quizDesc = (EditText) findViewById(R.id.quiz_desc);
                quizDesc.setText(quiz.getQuizDescription());
            }

            Button button = (Button) findViewById(R.id.create_quiz_button);
            button.setText("Edit quiz");
        }

        adapter = new QuestionAdapter(this, quiz);
        questionList.setAdapter(adapter);
    }

    public void addQuestion(View view)
    {
        Intent intent = new Intent(this, CreateQuestion.class);
        intent.putExtra("requestCode", 0);
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
                    adapter.notifyDataSetChanged();
                }
                break;
            }
            case(1) : {
                if(resultCode == Activity.RESULT_OK)
                {
                    quiz.getQuestions().remove(deleteItem);
                    Question question = (Question) data.getSerializableExtra("question");
                    quiz.getQuestions().add(deleteItem, question);
                    adapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }


    public void createQuiz(View view)
    {
        if(quiz.getQuestions().isEmpty())
        {
            //Hint user to add questions
            Toast.makeText(this, "You have to create questions!", Toast.LENGTH_SHORT).show();
            return;
        }


        EditText quizName = (EditText) findViewById(R.id.quiz_name);
        EditText quizDescription = (EditText) findViewById(R.id.quiz_desc);

        if(quizName.getText().length() == 0)
        {
            //Hint user to name the quiz
            Toast.makeText(this, "You have to name the quiz!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(QuizParser.fileExists(this, quizName.getText().toString()))
        {
            Toast.makeText(this, "This quiz already exists!", Toast.LENGTH_SHORT).show();
            return;
        }

        quiz.setQuizName(quizName.getText().toString());

        if(quizDescription.getText().length() > 0)
            quiz.setQuizDescription(quizDescription.getText().toString());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("quiz", quiz);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }

    private void editQuestion(Question question)
    {
        Intent intent = new Intent(this, CreateQuestion.class);
        intent.putExtra("question", question);
        intent.putExtra("requestCode", 1);
        startActivityForResult(intent, 1);
    }
}
