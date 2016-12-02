package com.apps.omar.quiz;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.omar.quiz.Backend.Answer;
import com.apps.omar.quiz.Backend.MultipleChoiceQuestion;
import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.YesNoQuestion;

public class CreateQuestion extends AppCompatActivity {

    LayoutInflater inflater;
    MyRadioGroup radioGroup = new MyRadioGroup();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addEditText();

    }


    public void toggleYesNo(View view)
    {
        View scrollable = (View) findViewById(R.id.answer_scroll);
        View answerText = (View) findViewById(R.id.answer_text_view);
        View yesOrNo = (View) findViewById(R.id.yes_or_no);

        if(((Switch)view).isChecked()) {
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
            return;
        }

        Switch yesNo = (Switch) findViewById(R.id.yes_no_switch);

        Question question = null;


        if(!yesNo.isChecked()) {

            ViewGroup parent = (ViewGroup) findViewById(R.id.linear_layout);


            question = new MultipleChoiceQuestion(questionName.getText().toString());

            boolean empty = true;
            boolean noCorrectAnswer = true;

            for (int i = 0; i < parent.getChildCount(); i++) {
                ViewGroup parent2 = (ViewGroup) parent.getChildAt(i);
                RadioButton yesNoButton = (RadioButton) parent2.getChildAt(0);
                EditText answer = (EditText) parent2.getChildAt(1);
                if (answer.getText().length() > 0) {
                    empty = false;
                    Answer newAnswer = new Answer(answer.getText().toString(), yesNoButton.isChecked());
                    ((MultipleChoiceQuestion)question).addAnswer(newAnswer);

                    if(yesNoButton.isChecked())
                    {
                        noCorrectAnswer = false;
                    }
                }
            }

            if (empty) {
                //Hint user to enter an answer
                return;
            }

            if(noCorrectAnswer)
            {
                //Hint user to set an answer as correct
                return;
            }
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
        EditText backet = (EditText) childll.getChildAt(1);

        if(child.getText().length() > 0) {

            if(child == backet)
            {
                addEditText();
            }
        }
        else
        {
            parent.removeView((View)child.getParent());
            backet.requestFocus();
        }

    }

    private void addEditText()
    {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.answer_layout, null, false);
        EditText editText = (EditText) view.getChildAt(1);
        RadioButton radioButton = (RadioButton) view.getChildAt(0);
        radioGroup.addButton(radioButton);
        addTextWatcher(editText);
        ViewGroup parent = (ViewGroup) findViewById(R.id.linear_layout);
        parent.addView(view);
    }

}
