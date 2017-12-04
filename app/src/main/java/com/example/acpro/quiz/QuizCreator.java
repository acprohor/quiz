package com.example.acpro.quiz;

import android.widget.Button;

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
    String temp[] = new String[10];

    String[] getSkills(Button button){
        final boolean[] test = {false};
        final String myMas[] = new String[7];
        for (int a = 0; a < 7; a++) {
            final int finalA = a;
            myRef.child(String.valueOf(a)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    temp[finalA] = String.valueOf(dataSnapshot.getValue(String.class));
                    myMas[finalA] = String.valueOf(dataSnapshot.getValue(String.class));
                    if (finalA == 6){
                        test[0] = true;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("ERROR to read ");
                }
            });
        }

        ready = true;
        button.setEnabled(false);
        if (test[0])return myMas;
        else
        return null;
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
}
