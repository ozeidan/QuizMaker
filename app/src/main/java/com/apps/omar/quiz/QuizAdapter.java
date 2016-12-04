package com.apps.omar.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.apps.omar.quiz.Backend.Quiz;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by omar on 02.12.16.
 */

public class QuizAdapter extends BaseAdapter {

    Context context;
    ArrayList<Quiz> data;
    private static LayoutInflater inflater = null;

    public QuizAdapter(Context context, ArrayList<Quiz> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        return vi;
    }
}
