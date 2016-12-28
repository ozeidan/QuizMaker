package com.apps.omar.quiz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.omar.quiz.Backend.Quiz;
import com.apps.omar.quiz.Backend.Score;
import com.apps.omar.quiz.R;

import java.util.ArrayList;

/**
 * Created by omar on 02.12.16.
 */

public class QuizAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<Quiz> data;
    private boolean showScore;

    public QuizAdapter(Context context, ArrayList<Quiz> data, boolean showScore) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showScore = showScore;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Quiz quiz = data.get(position);
        if (vi == null)
            vi = inflater.inflate(R.layout.quiz_list_element, null);

        TextView name = (TextView) vi.findViewById(R.id.name);
        name.setText(quiz.getQuizName());

        if (quiz.getQuizDescription() != null) {
            TextView desc = (TextView) vi.findViewById(R.id.description);
            desc.setText(quiz.getQuizDescription());
        }

        float score = showScore ? Score.loadScore(quiz.getId()) : 0f;


        // the green part of the list element which denotes the percentages of already correctly answered questions
        View bar = vi.findViewById(R.id.correct_percent_bar);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, score);
        bar.setLayoutParams(layoutParams);

        // the not-green invisible part
        View antiBar = vi.findViewById(R.id.incorrect_percent_bar);
        layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 100 - score);
        antiBar.setLayoutParams(layoutParams);

        return vi;
    }
}
