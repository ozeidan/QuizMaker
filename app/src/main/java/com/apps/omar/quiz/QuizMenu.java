package com.apps.omar.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
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
    ListView quizList;
    private ArrayList<Quiz> quizes;
    private int deleteQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_menu);




        int requestCode;

        Intent intent = getIntent();
        try {
            requestCode = (int) intent.getExtras().get("requestCode");
        }
        catch (NullPointerException e)
        {
            requestCode = 1;
        }


        quizes = QuizParser.loadQuizes(this);
        adapter = new QuizAdapter(this, quizes, requestCode == 0); //only show the scores when in play menu -> requestcode equals 0

        quizList = (ListView) findViewById(R.id.quiz_list);
        quizList.setAdapter(adapter);



        switch (requestCode) {
            //Editing
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
            //Playing
            case 0:
                quizList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        playQuiz(i);
                    }
                });

                break;
        }

        //set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.quiz_menu_toolbar);
        setSupportActionBar(toolbar);

        //get action bar object
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.quiz_menu_menu, menu);
        return true;
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
                adapter.notifyDataSetChanged();
            }
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.new_quiz_button:
                createQuiz(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        adapter.notifyDataSetChanged();
        switch(requestCode)
        {
            case (0): //returning activity created a quiz
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    Quiz quiz = (Quiz) data.getSerializableExtra("quiz");
                    QuizParser.saveQuiz(quiz, this);
                    adapter.notifyDataSetChanged();
                }
                break;
            }
            case (1): //returning activity changed a quiz
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    QuizParser.deleteQuiz(this, quizes.get(deleteQuiz));
                    Quiz newQuiz = (Quiz) data.getSerializableExtra("quiz");
                    QuizParser.saveQuiz(newQuiz, this);
                    adapter.notifyDataSetChanged();
                }
                break;
            }
            case (2): //returning acitvity played a quiz
            {
                if (resultCode == Activity.RESULT_OK) {
                    GameState gameState = (GameState) data.getExtras().get("gameState");
                    if (gameState.hasQuestion()) { //there are still questions, back to the playing activity
                        playQuiz(gameState);
                    }
                    else
                    {
                        // no more questions, view the score screen
                        Intent intent = new Intent(this, ScoreScreen.class);
                        intent.putExtra("gameState", gameState);
                        startActivityForResult(intent, 3); // probably not needed
                    }
                }
                break;
            }
        }
    }
}
