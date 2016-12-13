package com.apps.omar.quiz.Backend;

import android.content.Context;

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

    private static void initialize(Context context) {
        if (!initialized) {
            initialized = true;
        }
        else
        {
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

        return id;
    }

    public static void saveScore(long id, int score, Context context)
    {
        if(!initialized)
            throw new RuntimeException();

        if(!scoreBoard.scores.containsKey(id))
            throw new RuntimeException("Something went wrong again!");

        scoreBoard.scores.get(id).score = score;

        saveTable(context);
    }

    private static void saveTable(Context context)
    {
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


    private static class ScoreBoard implements Serializable {
        public HashMap<Long, ScoreStruct> scores;

        public long idCount = 0;
    }
    private static class ScoreStruct {
        public int score = 0;
        public int attempts = 0;
    }
}
