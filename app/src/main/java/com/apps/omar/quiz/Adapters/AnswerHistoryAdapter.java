package com.apps.omar.quiz.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.apps.omar.quiz.GameState;
import com.apps.omar.quiz.R;

/**
 * Created by omar on 15.12.16.
 */

public class AnswerHistoryAdapter extends BaseAdapter {
    private  LayoutInflater inflater = null;
    private GameState.AnswerHistory history;
    private Context context;


    public AnswerHistoryAdapter(Context context, GameState.AnswerHistory history) {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.history = history;
        this.context = context;
    }

    @Override
    public int getCount() {
        return history.getQuestionCount();
    }

    @Override
    public Object getItem(int position) {
        return history.getEntry(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameState.AnswerHistory.QuestionCorrectPair entry = history.getEntry(position);

        TextView textView = (TextView) inflater.inflate(R.layout.answer_history_list_item, null);
        textView.setText(entry.question.getQuestion());

        if(entry.correct)
        {
            textView.setBackgroundColor(Color.GREEN);
        }
        else
        {
            textView.setBackgroundColor(Color.RED);
        }

        return textView;
    }
}
