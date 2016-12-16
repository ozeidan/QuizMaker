package com.apps.omar.quiz.Backend;

/**
 * Created by omar on 02.12.16.
 * Class to save and load quizes to/from xml files.
 *
 */

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class QuizParser {
    private static boolean loaded = false;
    private static ArrayList<Quiz> quizList;

    public static void saveQuiz(Quiz quiz, Context context)
    {
        if (loaded) {
            quizList.add(quiz);
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            //root element quiz
            Element rootElement = doc.createElement("quiz");

            //set name attribute
            rootElement.setAttribute("name", quiz.getQuizName());


            //set description attribute
            if(quiz.getQuizDescription() != null)
                rootElement.setAttribute("description", quiz.getQuizDescription());

            //apend to document
            doc.appendChild(rootElement);

            //get questions and append them to root element
            ArrayList<Question> questions = quiz.getQuestions();
            for(Question question : questions)
            {
                Element questionElement = doc.createElement("question");

                //set question name
                questionElement.setAttribute("name", question.getQuestion());
                questionElement.setAttribute("yesNo", question instanceof YesNoQuestion ? "true" : "false");

                //set answers
                for (Answer answer : question.getAnswers()) {
                    Element answerElement = doc.createElement("answer");
                    answerElement.setAttribute("name", answer.getAnswer());
                    answerElement.setAttribute("correct", answer.isCorrect() ? "true" : "false");
                    questionElement.appendChild(answerElement);
                }


                rootElement.appendChild(questionElement);
            }

            //transform to XML
            TransformerFactory transformerFactory    = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            File file = new File(context.getFilesDir() + "/quizes/", quiz.getQuizName());
            StreamResult result = new StreamResult(file);


            //save
            transformer.transform(domSource, result);
            printFile(file);
        }
        catch(ParserConfigurationException | TransformerException e)
        {
            e.printStackTrace();
        }

    }

    public static ArrayList<Quiz> loadQuizes(Context context)
    {
        if (loaded) {
            return quizList;
        }

        ArrayList<Quiz> quizList = new ArrayList<>();


            File folder = new File(context.getFilesDir() + "/quizes/");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            try {
                DocumentBuilder builder = factory.newDocumentBuilder();


                if (folder.listFiles() != null) {
                    for (File file : folder.listFiles()) {
                        try {
                            Quiz quiz = new Quiz();

                            Document doc = builder.parse(file);

                            doc.getDocumentElement().normalize();

                            quiz.setQuizName(doc.getDocumentElement().getAttribute("name"));
                            quiz.setQuizDescription(doc.getDocumentElement().getAttribute("description"));

                            NodeList questions = doc.getElementsByTagName("question");

                            for (int i = 0; i < questions.getLength(); i++) {
                                Element questionElement = (Element) questions.item(i);

                                Question question;
                                String name = questionElement.getAttribute("name");

                                NodeList answers = questionElement.getChildNodes();
                                ArrayList<Answer> answerObs = new ArrayList<>();

                                for (int j = 0; j < answers.getLength(); j++) {
                                    Element answerElement = (Element) answers.item(j);

                                    String answerName = answerElement.getAttribute("name");
                                    boolean correct = answerElement.getAttribute("correct").equals("true");

                                    Answer answer = new Answer(answerName, correct);
                                    answerObs.add(answer);
                                }


                                if (questionElement.getAttribute("yesNo").equals("true")) {
                                    question = new YesNoQuestion(name, answerObs);
                                } else {
                                    question = new Question(name, answerObs);
                                }

                                quiz.addQuestion(question);
                            }

                            quizList.add(quiz);

                        }
                        catch(SAXException | IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch(ParserConfigurationException e)
            {
                e.printStackTrace();
            }


        return quizList;

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
        for(File file : folder.listFiles())
        {
            Log.d("QuizParser", file.getName());
        }
    }

    public static void printFile(File file)
    {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
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
