package com.apps.omar.quiz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.omar.quiz.Backend.Answer;
import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.YesNoQuestion;

import java.util.ArrayList;

public class CreateQuestion extends AppCompatActivity {

    LayoutInflater inflater;
    MyRadioGroup radioGroup = new MyRadioGroup();
    Question question;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        Intent intent = getIntent();


        if((int)(intent.getExtras().get("requestCode")) == 1)
        {
            question = (Question) intent.getExtras().get("question");
            initWithQuestion(question);
        }

        addEditText(false, "");


        Toolbar toolbar = (Toolbar) findViewById(R.id.create_question_toolbar);
        setSupportActionBar(toolbar);


        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.finish_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.finish_button:
            {
                createButton(null);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void toggleYesNo(View view)
    {
        View scrollable = findViewById(R.id.answer_scroll);
        View answerText = findViewById(R.id.answer_text_view);
        View yesOrNo = findViewById(R.id.yes_or_no);

        if (answerText.getVisibility() == View.VISIBLE) {
            answerText.setVisibility(View.INVISIBLE);
            scrollable.setVisibility(View.INVISIBLE);
            yesOrNo.setVisibility(View.VISIBLE);
        }
        else {
            answerText.setVisibility(View.VISIBLE);
            scrollable.setVisibility(View.VISIBLE);
            yesOrNo.setVisibility(View.INVISIBLE);
        }
    }



    public void createButton(View view)
    {
        TextView questionName = (TextView) findViewById(R.id.question_name);
        if(questionName.getText().length() == 0)
        {
            //Hint user to enter a name
            Toast.makeText(this, "You have to enter a question!", Toast.LENGTH_SHORT).show();
            return;
        }

        Switch yesNo = (Switch) findViewById(R.id.yes_no_switch);
        Question question = null;


        if(!yesNo.isChecked()) {

            ViewGroup parent = (ViewGroup) findViewById(R.id.linear_layout);
            ArrayList<Answer> answers = new ArrayList<>();

            boolean noCorrectAnswer = true;

            for (int i = 0; i < parent.getChildCount(); i++) {
                ViewGroup parent2 = (ViewGroup) parent.getChildAt(i);
                RadioButton yesNoButton = (RadioButton) parent2.findViewById(R.id.answer_is_correct);
                EditText answer = (EditText) parent2.findViewById(R.id.answer_edit_text);
                if (answer.getText().length() > 0) {
                    if(yesNoButton.isChecked())
                        noCorrectAnswer = false;

                    Answer newAnswer = new Answer(answer.getText().toString(), yesNoButton.isChecked());
                    answers.add(newAnswer);
                }
            }

            if (answers.isEmpty()) {
                //Hint user to enter an answer
                Toast.makeText(this, "You have to enter answers!", Toast.LENGTH_SHORT).show();
                return;
            }

            if(noCorrectAnswer)
            {
                //Hint user to set an answer as correct
                Toast.makeText(this, "Which one is the correct answer?", Toast.LENGTH_SHORT).show();
                return;
            }

            question = new Question(questionName.getText().toString(), answers);
        }
        else if(yesNo.isChecked()) //just to clarify
        {
            Switch yesOrNo = (Switch) findViewById(R.id.yes_or_no);
            question = new YesNoQuestion(questionName.getText().toString(), yesOrNo.isChecked()); //change this too
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("question", question);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }



    private void addTextWatcher(final EditText text)
    {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkForm(text);
            }
        });

    }

    private void checkForm(EditText child)
    {
        LinearLayout parent = (LinearLayout) findViewById(R.id.linear_layout);

        LinearLayout childll = (LinearLayout) parent.getChildAt(parent.getChildCount() - 1);
        EditText backet = (EditText) childll.findViewById(R.id.answer_edit_text);

        if(child.getText().length() > 0) {

            if(child == backet)
            {
                addEditText(false, "");
            }
        }
        else
        {
            if(parent.indexOfChild((View) child.getParent()) == parent.getChildCount() - 2)
            {
                parent.removeView(childll);
            }
            else {
                parent.removeView((View) child.getParent());
                backet.requestFocus();
            }
        }

    }

    private ViewGroup addEditText(boolean checked, String text)
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.answer_add_layout, null, false);
        EditText answerText = (EditText) view.findViewById(R.id.answer_edit_text);
        RadioButton isCorrect = (RadioButton) view.findViewById(R.id.answer_is_correct);

        answerText.setText(text);
        isCorrect.setChecked(checked);

        radioGroup.addButton(isCorrect);
        addTextWatcher(answerText);

        ViewGroup parent = (ViewGroup) findViewById(R.id.linear_layout);
        parent.addView(view);

        return view;
    }

    private void initWithQuestion(Question question) {
        ((EditText) findViewById(R.id.question_name)).setText(question.getQuestion());

        if (question instanceof YesNoQuestion) {
            ((Switch) findViewById(R.id.yes_no_switch)).setChecked(true);
            ((Switch) findViewById(R.id.yes_or_no)).setChecked(((YesNoQuestion) question).isYesOrNo());
            toggleYesNo(new View(this));
        } else {
            for (Answer answer : question.getAnswers()) {
                addEditText(answer == question.getCorrectAnswer(), answer.getAnswer());
            }
        }
    }


}
