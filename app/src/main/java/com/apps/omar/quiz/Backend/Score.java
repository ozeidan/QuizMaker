package com.apps.omar.quiz.Backend;

import java.io.Serializable;
import java.util.HashMap;


public class Score {
    private static long identifierCount;
    private static boolean initialized = false;

    private static void initialize() {
        if (!initialized) {
            //load stuff

            initialized = true;
        }
    }

    public static void addQuiz(Quiz quiz, int score) {
        if (quiz.getId() != -1) {
            quiz.setId(identifierCount);
        }
    }

    public static void initQuizId(Quiz quiz) {
        if (quiz.getId() == -1) {
            quiz.setId(identifierCount);
            identifierCount++;
        }
    }

    private class ScoreBoard implements Serializable {
        public HashMap<Long, Integer> scores;

        private class ScorePair {
            public int score;
        }
    }
}
