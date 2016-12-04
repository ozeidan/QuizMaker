package com.apps.omar.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apps.omar.quiz.Backend.Question;
import com.apps.omar.quiz.Backend.Quiz;

import java.util.ArrayList;

/**
 * Created by omar on 03.12.16.
 */

public class QuestionAdapter extends BaseAdapter {
    Context context;
    Quiz data;
    private static LayoutInflater inflater = null;

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
            vi = new TextView(context);

        ((TextView)vi).setText(question.getQuestion());

        return vi;
    }
}
