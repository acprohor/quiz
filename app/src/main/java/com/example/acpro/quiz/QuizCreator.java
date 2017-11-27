package com.example.acpro.quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizCreator {

    private String question;
    private String rightAnswer;
    private int quizArraySize = 0;

    ArrayList <String> quiz = new ArrayList<>();
    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    String[][] quizData = {
            //{Country, Right Answer, choice1, choice2, choice3}
            {"Belarus", "Minsk", "Moscow", "Seoul", "Bangkok"},
            {"Russia", "Moscow", "Brest", "Los Angeles", "Moxico"},
            {"Spain", "Madrid", "London", "Paris", "Singapore"},
            {"test1", "rightAnswer", "wrong", "wrong", "wrong"},
            {"test2", "rightAnswer", "wrong", "wrong", "wrong"},
            {"test3", "rightAnswer", "wrong", "wrong", "wrong"},
    };

    void createQuizArray(){
        for (int i = 0; i < quizData.length; i++){
            // Prepare array.
            ArrayList<String> tmpArray = new ArrayList<>();
            tmpArray.add(quizData[i][0]);   // Country
            tmpArray.add(quizData[i][1]);   // Right Answer
            tmpArray.add(quizData[i][2]);   // Choice 1
            tmpArray.add(quizData[i][3]);   // Choice 2
            tmpArray.add(quizData[i][4]);   // Choice 3

            // Add tmpArray to quizArray.
            quizArray.add(tmpArray);
        }
        quizArraySize = quizArray.size();
        Collections.shuffle(quizArray);
    }

    void createNewSomething(){
        for (int i = 0; i<quizArraySize; i++){
            ArrayList<String> localQuiz = quizArray.get(i);
            String questionLocal = localQuiz.get(0);
            String rightAnswerLocal = localQuiz.get(1);
            localQuiz.remove(0);
            Collections.shuffle(localQuiz);
            localQuiz.add(0,questionLocal);
            localQuiz.add(1,rightAnswerLocal);
            quizArray.set(i,localQuiz);
        }

    }
}
