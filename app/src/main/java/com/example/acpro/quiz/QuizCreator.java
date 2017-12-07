package com.example.acpro.quiz;

import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class QuizCreator {
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef = database.getReference("skills");

    boolean ready = false;

    private String question;
    private String rightAnswer;
    private int quizArraySize = 0;


    //ArrayList <String> quiz = new ArrayList<>();
    ArrayList<ArrayList<String>> quizArray = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<String> skills = new ArrayList<>();



    String[][] quizData = {
            //{Country, Right Answer, choice1, choice2, choice3}
            {"Belarus", "Minsk", "Moscow", "Seoul", "Bangkok"},
            {"Russia", "Moscow", "Brest", "Los Angeles", "Moxico"},
            {"Spain", "Madrid", "London", "Paris", "Singapore"},
            {"test1", "rightAnswer", "wrong", "wrong", "wrong"},
            {"test2", "rightAnswer", "wrong", "wrong", "wrong"},
            {"test3", "rightAnswer", "wrong", "wrong", "wrong"},
    };



    void createQuery(){
        names.add("string");
        Query getSkillsList = myRef;
        getSkillsList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("try to add into names");
                String value = dataSnapshot.getValue(String.class);
                System.out.println("value " + value);
                names.add(value);
                System.out.println("value is added ");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    String temp[] = new String[11];

    String[] getSkills(Button button){
        final String myMas[] = new String[7];
        for (int a = 0; a < 7; a++) {
            final int finalA = a;
            myRef.child(String.valueOf(a)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //temp[finalA] = String.valueOf(dataSnapshot.getValue(String.class));
                    //myMas[finalA] = String.valueOf(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("ERROR to read ");
                }
            });
        }

        return myMas;
    }

    void setSkillsOnButton(final Button[] buttons){
        final String myMas[] = new String[7];

        for (int a = 1; a < 4; a++) {
            final int finalA = a;
            myRef.child(String.valueOf(a)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //buttons[finalA].setText(String.valueOf(dataSnapshot.getValue(String.class)));
                    buttons[finalA].setText(String.valueOf(dataSnapshot.getValue(String.class)));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("ERROR to read ");
                }
            });
        }

    }

    void showSkills(){
        System.out.println("try to show skills");
        for (int i = 0; i < temp.length; i ++ ){
            System.out.println(temp[i]);
        }
    }

    void showList(){
        System.out.println("try to show");
        for (int i = 0; i < names.size(); i ++ ){
            System.out.println(names.get(i));
        }
    }


    void createQuizArray(){
        quizArray.clear();
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

    private int mixedButtonsNum[][];

    private int quizIdArray[][] = new int[11][6];

    void createIdArray(int level){
        Random random = new Random();

        mixedButtonsNum = new int[11][level];

        for (int a = 0; a < 11; a++) {      // создание матрицы распределения кнопок
            for (int i = 0; i < level; i++) {
                mixedButtonsNum[a][i] = random.nextInt(level);
                if (i > 0) {
                    for (int j = i - 1; j >= 0; j--) {
                        while (mixedButtonsNum[a][i] == mixedButtonsNum[a][j]) {
                            mixedButtonsNum[a][i] = random.nextInt(level);
                            j = i - 1;
                        }
                    }
                }
            }
        }
        System.out.println("Матрица с ID кнопок ");
        for (int i=0; i < 11; i++){
            for (int j =0 ; j<level;j++){
                System.out.print(mixedButtonsNum[i][j] + " ");
            }
            System.out.println("-----");
        }

        int randomSkillsNum[] = new int[11];        // 10 случайных способностей которые нужно отгадать
        for (int i = 0; i < randomSkillsNum.length; i++){
            randomSkillsNum[i] = random.nextInt(115);
            if (i > 0 ) {
                for (int j = i-1; j >= 0; j--) {
                    while (randomSkillsNum[i] == randomSkillsNum[j]) {
                        randomSkillsNum[i] = random.nextInt(115);
                        j = i-1;
                    }
                }
            }
        }


        for (int i = 0; i < 11; i++) {          // записываются в первые элементы матрицы
            quizIdArray[i][0] = randomSkillsNum[i];
        }

        for (int i = 0; i < 11; i++){           // создание остальной матрицы
            for (int j = 1; j < 6; j++){
                quizIdArray[i][j] = random.nextInt(115);
                for (int k = j-1; k >= 0; k--){
                    while (quizIdArray[i][j] == quizIdArray[i][k]){
                        quizIdArray[i][j] = random.nextInt(115);
                        k = j-1;
                    }
                }
            }
        }
    }
    private String rightRightAnswer = "netotveta";
    String fillButtons(final Button[] buttons, final TextView textView, int secNum, int level){

        final int [][] innerMixedButtonsNum = mixedButtonsNum;

        final int sN = secNum;

        myRef.child(String.valueOf(quizIdArray[sN][0])).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //rightRightAnswer = sN + " " + "0" + String.valueOf(dataSnapshot.getValue(String.class)) + " " + quizIdArray[sN][0];
                textView.setText(sN + " " + "0" + String.valueOf(dataSnapshot.getValue(String.class)) + " " + quizIdArray[sN][0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        for (int i = 0; i < level; i++){
            final int finalI = i;
            myRef.child(String.valueOf(quizIdArray[sN][finalI])).addValueEventListener(new ValueEventListener() {
                                                                                      @Override
                                                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                          buttons[mixedButtonsNum[sN][finalI]].setText(sN + " " + finalI + String.valueOf(dataSnapshot.getValue(String.class)) + " " + quizIdArray[sN][finalI]);
                                                                                      }

                                                                                      @Override
                                                                                      public void onCancelled(DatabaseError databaseError) {
                                                                                          System.out.println("connection failed");
                                                                                      }
                                                                                  });

        }

        return rightRightAnswer;
    }

}
