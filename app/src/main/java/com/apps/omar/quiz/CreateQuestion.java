package com.apps.omar.quiz;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.apps.omar.quiz.Backend.Answer;
import com.apps.omar.quiz.Backend.MultipleChoiceQuestion;
import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.YesNoQuestion;

public class CreateQuestion extends AppCompatActivity {

    FormExpand formExpand = new FormExpand(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        final EditText v = (EditText) findViewById(R.id.answer1);

        addTextWatcher(v);
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
                EditText newText = formExpand.checkForm(text , (ViewGroup) findViewById(R.id.linear_layout));
                if(newText != null)
                {
                    addTextWatcher(newText);
                }
            }
        });

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

            ViewGroup answers = (ViewGroup) findViewById(R.id.linear_layout);


            question = new MultipleChoiceQuestion(questionName.getText().toString());

            boolean empty = true;

            for (int i = 0; i < answers.getChildCount(); i++) {
                EditText answer = (EditText) answers.getChildAt(i);
                if (answer.getText().length() > 0) {
                    empty = false;
                    Answer newAnswer = new Answer(answer.getText().toString(), true); //change this plessssssssseee
                    ((MultipleChoiceQuestion)question).addAnswer(newAnswer);
                }
            }

            if (empty) {
                //Hint user to enter an answer
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

    private void finishActivity()
    {

    }

}
