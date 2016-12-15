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
import android.widget.ListView;

import com.apps.omar.quiz.Adapters.QuizAdapter;
import com.apps.omar.quiz.Backend.Quiz;
import com.apps.omar.quiz.Backend.QuizParser;

import java.util.ArrayList;

public class QuizMenu extends AppCompatActivity {

    QuizAdapter adapter;
    ArrayList<Quiz> quizes;
    ListView quizList;
    private int deleteQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_menu);

        quizes = QuizParser.loadQuizes(this);
        adapter = new QuizAdapter(this, quizes);

        quizList = (ListView) findViewById(R.id.quiz_list);
        quizList.setAdapter(adapter);

        Intent intent = getIntent();
        int requestCode = (int) intent.getExtras().get("requestCode");

        switch (requestCode) {
            case 1: {
                quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        editQuiz(i);
                    }
                });
                registerForContextMenu(quizList);
                break;
            }
            case 0:
                quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        playQuiz(i);
                    }
                });
                findViewById(R.id.new_quiz_button).setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == quizList) {
            menu.add(Menu.NONE, 1, Menu.NONE, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: {
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                QuizParser.deleteQuiz(this, (Quiz) adapter.getItem(info.position));
                quizes.remove(info.position);
                adapter.notifyDataSetChanged();
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void createQuiz(View view) {
        Intent intent = new Intent(this, CreateQuiz.class);
        intent.putExtra("requestCode", 0);
        startActivityForResult(intent, 0);
    }

    public void editQuiz(int position)
    {
        deleteQuiz = position;
        Intent intent = new Intent(this, CreateQuiz.class);
        intent.putExtra("quiz", quizes.get(position));
        intent.putExtra("requestCode", 1);
        startActivityForResult(intent, 1);
    }


    public void playQuiz(int position) {

        playQuiz(new GameState(quizes.get(position)));
    }

    public void playQuiz(GameState gameState) {
        Intent intent = new Intent(this, PlayQuiz.class);
        intent.putExtra("gameState", gameState);
        intent.putExtra("requestCode", 2);
        startActivityForResult(intent, 2);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case(0):
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    Quiz quiz = (Quiz) data.getSerializableExtra("quiz");
                    quizes.add(quiz);
                    adapter.notifyDataSetChanged();

                    QuizParser.saveQuiz(quiz, this);
                }
                break;
            }
            case(1):
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    QuizParser.deleteQuiz(this, quizes.get(deleteQuiz));
                    quizes.remove(deleteQuiz);
                    Quiz newQuiz = (Quiz) data.getSerializableExtra("quiz");
                    quizes.add(deleteQuiz, newQuiz);
                    adapter.notifyDataSetChanged();

                    QuizParser.saveQuiz(newQuiz, this);
                }
                break;
            }
            case (2): {
                if (resultCode == Activity.RESULT_OK) {
                    GameState gameState = (GameState) data.getExtras().get("gameState");
                    if (gameState.hasQuestion()) {
                        playQuiz(gameState);
                    }
                    else
                    {
                        Intent intent = new Intent(this, ScoreScreen.class);
                        intent.putExtra("gameState", gameState);
                        startActivity(intent);
                    }
                }
            }
        }
    }
}
