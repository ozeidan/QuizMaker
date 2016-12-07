package com.apps.omar.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.Quiz;

/**
 * Created by omar on 03.12.16.
 */

public class QuestionAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    Context context;
    Quiz data;

    public QuestionAdapter(Context context, Quiz data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.getQuestions().size();
    }

    @Override
    public Object getItem(int position) {
        return data.getQuestions().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Question question = data.getQuestions().get(position);
        if (vi == null)
            vi = inflater.inflate(R.layout.question_list_item, null);

        ((TextView)vi).setText(question.getQuestion());

        return vi;
    }
}
