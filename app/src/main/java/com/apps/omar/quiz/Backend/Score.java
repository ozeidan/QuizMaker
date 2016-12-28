package com.apps.omar.quiz.Backend;

import android.content.Context;
import android.widget.Toast;

import com.apps.omar.quiz.GameState;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;


public class Score {
    private static ScoreBoard scoreBoard;
    private static boolean initialized = false;
    private static String FILENAME = "score";
    private static Context context;

    public static void initialize(Context context) {
        if (!initialized)
        {
            Score.context = context;

            File file = new File(context.getFilesDir(), FILENAME);

            try {
                FileInputStream f_in = new FileInputStream(file);
                ObjectInputStream obj_in = new ObjectInputStream(f_in);

                Object obj = obj_in.readObject();

                if(obj instanceof ScoreBoard)
                {
                    scoreBoard = (ScoreBoard) obj;
                }
                else
                {
                    throw new RuntimeException("Something went wrong!");
                }
            }
            catch(FileNotFoundException e)
            {
                scoreBoard = new ScoreBoard();
            }
            catch(IOException | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            finally
            {
                initialized = true;
            }
        }
    }

    public static long getId(Quiz quiz)
    {
        if(!initialized)
            throw new RuntimeException();

        if(quiz.getId() != -1)
            throw new RuntimeException();

        long id = scoreBoard.idCount++;

        if(scoreBoard.scores.containsKey(id))
            throw new RuntimeException();

        scoreBoard.scores.put(id, new ScoreStruct());

        saveTable();

        return id;
    }


    public static void saveScore(GameState gameState)
    {
        if(!initialized)
            throw new RuntimeException();

        long id = gameState.getId();

        if(!scoreBoard.scores.containsKey(id))
            throw new RuntimeException("Something went wrong again!");

        updateStats(gameState);

        float score = (float) gameState.getCorrectlyAnswered() / gameState.getAnswered() * 100;

        if (score > scoreBoard.scores.get(id).score)
            scoreBoard.scores.get(id).score = score;

        saveTable();
    }

    private static void updateStats(GameState gameState) {
        scoreBoard.quizAttempts++;
        scoreBoard.answerAttempts += gameState.getAnswered();
        scoreBoard.correctAnswers += gameState.getCorrectlyAnswered();
    }

    public static float loadScore(long id)
    {
        if (!initialized)
            throw new RuntimeException();

        if (!scoreBoard.scores.containsKey(id))
            throw new RuntimeException();

        return scoreBoard.scores.get(id).score;
    }

    private static void saveTable() {
        try {
            File file = new File(context.getFilesDir(), FILENAME);

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            FileOutputStream f_out = new FileOutputStream(file);

            ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

            obj_out.writeObject(scoreBoard);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void debug() {
        Toast.makeText(context, Long.toString(scoreBoard.idCount), Toast.LENGTH_SHORT).show();
    }

    public static long getAttempts() {
        return scoreBoard.quizAttempts;
    }

    public static long getAnswerAttempts() {
        return scoreBoard.answerAttempts;
    }

    public static long getCorrectAnswers() {
        return scoreBoard.correctAnswers;
    }

    private static class ScoreBoard implements Serializable {
        public HashMap<Long, ScoreStruct> scores = new HashMap<>();

        private long idCount = 0;
        private long quizAttempts = 0;
        private long answerAttempts = 0;
        private long correctAnswers = 0;
    }

    private static class ScoreStruct implements Serializable {
        public float score = 0;
        //public int attempts = 0;
    }


}
