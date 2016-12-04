package com.apps.omar.quiz.Backend;

/**
 * Created by omar on 02.12.16.
 * Class to save and load quizes to/from xml files.
 *
 */
import android.content.Context;
import android.util.Log;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.ArrayList;

public class QuizParser {
    public static void saveQuiz(Quiz quiz, Context context)
    {

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

                String questionType = "";

                //each question type has to be saved differently
                if(question instanceof MultipleChoiceQuestion) {
                    questionType = "multipleChoice";

                    for(Answer answer : ((MultipleChoiceQuestion) question).getAnswers())
                    {
                        Element answerElement = doc.createElement("answer");
                        answerElement.setAttribute("correctAnswer", answer.correctAnswer ? "true" : "false");
                        answerElement.setAttribute("name", answer.answer);
                        questionElement.appendChild(answerElement);
                    }
                }
                else if(question instanceof YesNoQuestion) {
                    questionType = "yesNo";
                    //set the correct answer to the yes/no question
                    questionElement.setAttribute("yesOrNo", ((YesNoQuestion) question).isYesOrNo() ? "true" : "false");
                }
                else
                    throw new RuntimeException("question must be either multiple choice or yes no");

                questionElement.setAttribute("questionType", questionType);
                rootElement.appendChild(questionElement);
            }

            //transform to XML
            TransformerFactory transformerFactory    = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            File file = new File(context.getFilesDir() + "/quizes/", quiz.getQuizName());
            file.getParentFile().mkdirs();
            file.createNewFile();
            StreamResult result = new StreamResult(file);


            //save
            transformer.transform(domSource, result);



        }
        catch(ParserConfigurationException | TransformerException | IOException e)
        {
            e.printStackTrace();
        }

    }

    public static ArrayList<Quiz> loadQuizes(Context context)
    {

        ArrayList<Quiz> quizList = new ArrayList<>();

        try {
            File folder = new File(context.getFilesDir() + "/quizes/");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();


            for(File file : folder.listFiles())
            {
                Quiz quiz = new Quiz();

                Document doc = builder.parse(file);

                doc.getDocumentElement().normalize();

                quiz.setQuizName(doc.getDocumentElement().getAttribute("name"));
                quiz.setQuizDescription(doc.getDocumentElement().getAttribute("description"));

                NodeList questions = doc.getElementsByTagName("question");

                for(int i = 0; i < questions.getLength(); i ++)
                {
                    Question question;
                    Element questionElement = (Element) questions.item(i);

                    String questionType = questionElement.getAttribute("questionType");

                    if(questionType.equals("multipleChoice"))
                    {
                        question = new MultipleChoiceQuestion(questionElement.getAttribute("name"));

                        NodeList answers = questionElement.getChildNodes();

                        for(int j = 0; j < answers.getLength(); j++)
                        {

                            Element answerElement = (Element) answers.item(j);

                            String answerName = answerElement.getAttribute("name");
                            boolean correctAnswer = answerElement.getAttribute("correctAnswer").equals("true");

                            Answer answer = new Answer(answerName, correctAnswer);
                            ((MultipleChoiceQuestion) question).addAnswer(answer);
                        }

                    }
                    else if(questionType.equals("yesNo"))
                    {
                        question = new YesNoQuestion(questionElement.getAttribute("name"), (questionElement.getAttribute("yesOrNo").equals("true")));
                    }
                    else
                    {
                        throw new RuntimeException("Questiontype unknown");
                    }

                    quiz.addQuestion(question);
                }

                quizList.add(quiz);

            }

        }


        catch(ParserConfigurationException | SAXException | IOException e)
        {
            e.printStackTrace();
        }
        finally {
            return quizList;
        }
    }

    public static boolean deleteQuiz(Context context, Quiz quiz)
    {
        File file = new File(context.getFilesDir() + "/quizes/", quiz.getQuizName());

        return file.delete();
    }

    public static boolean fileExists(Context context, String fileName)
    {
        File file = new File(context.getFilesDir() + "/quizes/", fileName);
        return file.exists() && !file.isDirectory();
    }
}
