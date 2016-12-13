package com.apps.omar.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.Quiz;
import com.apps.omar.quiz.Backend.QuizParser;
import com.apps.omar.quiz.Backend.Score;

public class CreateQuiz extends AppCompatActivity {
    QuestionAdapter adapter;
    private Quiz quiz = new Quiz();
    private int deleteItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        //set custom adapter on questoin list
        ListView questionList = (ListView) findViewById(R.id.question_list);
        adapter = new QuestionAdapter(this, quiz);
        questionList.setAdapter(adapter);

        //when clicking a question we want to edit it
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

            initWithQuiz(quiz);
        }

        adapter = new QuestionAdapter(this, quiz);
        questionList.setAdapter(adapter);

        //setcontextview
        registerForContextMenu(questionList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, 1, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                quiz.removeQuestion(info.position);
                adapter.notifyDataSetChanged();
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    // when clicking the add questoin button we wills start the createquetsion activity
    public void addQuestion(View view)
    {
        Intent intent = new Intent(this, CreateQuestion.class);
        intent.putExtra("requestCode", 0);
        startActivityForResult(intent, 0);
    }

    private void editQuestion(Question question) {
        Intent intent = new Intent(this, CreateQuestion.class);
        intent.putExtra("question", question);
        intent.putExtra("requestCode", 1);
        startActivityForResult(intent, 1);
    }

    // receive the created question and add it to the list
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (0) : {
                // question was succesfully created
                if (resultCode == Activity.RESULT_OK) {
                    Question question = (Question) data.getSerializableExtra("question");
                    // add to quiz
                    quiz.addQuestion(question);
                    // notify adapter to render it on the screen
                    adapter.notifyDataSetChanged();
                }
                break;
            }
            case(1) : {
                if(resultCode == Activity.RESULT_OK)
                {
                    // delete old quiz and replace it with new one if the request was to edit a quiz
                    quiz.getQuestions().remove(deleteItem);
                    Question question = (Question) data.getSerializableExtra("question");
                    // add the edited quiz at the place of the deleted one
                    // should be implemented inside quiz class
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

        //Only when adding new quizes
        if ((int) getIntent().getExtras().get("requestCode") == 0) {
            if (QuizParser.fileExists(this, quizName.getText().toString())) {
                Toast.makeText(this, "This quiz already exists!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        quiz.setQuizName(quizName.getText().toString());

        if(quizDescription.getText().length() > 0)
            quiz.setQuizDescription(quizDescription.getText().toString());

        Score.initQuizId(quiz);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("quiz", quiz);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();

    }


    private void initWithQuiz(Quiz quiz)
    {
        EditText quizName = (EditText) findViewById(R.id.quiz_name);
        quizName.setText(quiz.getQuizName());

        if (quiz.getQuizDescription() != null) {
            EditText quizDesc = (EditText) findViewById(R.id.quiz_desc);
            quizDesc.setText(quiz.getQuizDescription());
        }

        Button button = (Button) findViewById(R.id.create_quiz_button);
        button.setText("Edit quiz");
    }
}
