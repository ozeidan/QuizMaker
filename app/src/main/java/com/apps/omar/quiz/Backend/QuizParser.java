package com.apps.omar.quiz.Backend;

/**
 * Created by omar on 02.12.16.
 * Class to save and load quizes to/from xml files.
 *
 */

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;


public class QuizParser {
    private static boolean loaded = false;
    private static ArrayList<Quiz> quizList = new ArrayList<>();


    public static ArrayList<Quiz> saveQuiz(Quiz quiz, Context context)
    {
        if (loaded) {
            quizList.add(quiz);
        }

        try {

            XmlSerializer serializer = Xml.newSerializer();
            StringWriter stringWriter = new StringWriter();
            serializer.setOutput(stringWriter);

            //root element quiz
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "quiz");

            //set name attribute
            serializer.attribute("", "name", quiz.getQuizName());


            //save the id
            serializer.attribute("", "id", Long.toString(quiz.getId()));


            //set description attribute
            if(quiz.getQuizDescription() != null)
                serializer.attribute("", "description", quiz.getQuizDescription());

            //get questions and append them to root element
            ArrayList<Question> questions = quiz.getQuestions();
            for(Question question : questions)
            {
                serializer.startTag("", "question");

                //set question name
                serializer.attribute("", "name", question.getQuestion());
                serializer.attribute("", "yesNo", question instanceof YesNoQuestion ? "true" : "false");

                //set answers
                for (Answer answer : question.getAnswers()) {
                    serializer.startTag("", "answer");
                    serializer.attribute("", "name", answer.getAnswer());
                    serializer.attribute("", "correct", answer.isCorrect() ? "true" : "false");
                    serializer.endTag("", "answer");
                }

                serializer.endTag("", "question");

            }

            serializer.endTag("", "quiz");
            serializer.flush();

            //save
            File file = new File(context.getFilesDir() + "/quizes/", quiz.getQuizName());
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream fileos = new FileOutputStream(file);

            fileos.write(stringWriter.toString().getBytes());
            fileos.close();

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return quizList;

    }

    public static ArrayList<Quiz> loadQuizes(Context context) {
        if (loaded)
            return quizList;


        XmlPullParser parser = Xml.newPullParser();


        File folder = new File(context.getFilesDir() + "/quizes/");

        if (folder.listFiles() == null) {
            return quizList;
        }


        for (File file : folder.listFiles()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                parser.setInput(fis, null);

                int eventType = parser.getEventType();

                Quiz quiz = null;
                Question question = null;
                Answer answer = null;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case (XmlPullParser.START_DOCUMENT): {
                            quiz = null;
                            break;
                        }
                        case (XmlPullParser.START_TAG): {
                            switch (parser.getName()) {
                                case ("quiz"): {
                                    quiz = new Quiz(Long.parseLong(parser.getAttributeValue("", "id")));
                                    quiz.setQuizName((parser.getAttributeValue("", "name")));
                                    quiz.setQuizDescription(parser.getAttributeValue("", "description"));
                                    break;
                                }
                                case ("question"): {
                                    boolean yesNo = parser.getAttributeValue("", "yesNo").equals("true");
                                    question = yesNo ? new YesNoQuestion() : new Question();
                                    question.setQuestion(parser.getAttributeValue("", "name"));
                                    break;
                                }
                                case ("answer"): {
                                    answer = new Answer();
                                    answer.setAnswer(parser.getAttributeValue("", "name"));
                                    answer.setCorrect(parser.getAttributeValue("", "correct").equals("true"));
                                    break;
                                }
                            }
                            break;
                        }
                        case (XmlPullParser.END_TAG): {
                            switch (parser.getName()) {
                                case ("quiz"): {
                                    quizList.add(quiz);
                                    break;
                                }
                                case ("question"): {
                                    quiz.addQuestion(question);
                                    break;
                                }
                                case ("answer"): {
                                    question.addAnswer(answer);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    eventType = parser.next();
                }

            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }
        loaded = true;
        return QuizParser.quizList;
    }

    public static boolean deleteQuiz(Context context, Quiz quiz)
    {
        File file = new File(context.getFilesDir() + "/quizes/", quiz.getQuizName());

        if (loaded)
            quizList.remove(quiz);

        return file.delete();
    }

    public static boolean fileExists(Context context, String fileName)
    {
        File file = new File(context.getFilesDir() + "/quizes/", fileName);
        return file.exists() && !file.isDirectory();
    }

    public static void showFiles(Context context)
    {
        File folder = new File(context.getFilesDir() + "/quizes/");
        Log.d("QuizParser", "List of existing quizes:");
        if (folder.exists()) {
            for (File file : folder.listFiles()) {
                Log.d("QuizParser", file.getName());
            }
        }
    }

    private static void printFile(File file)
    {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while((line = bufferedReader.readLine()) != null)
            {
                Log.d("QuizParser", line);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
