package com.example.acpro.quiz;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class QuizCreator{

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference myRef = database.getReference("skills");
    private static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

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

    void fillButtons(final Button[] buttons, final TextView textView, final ImageView imageView, int secNum, int level, final Context context){

        final int sN = secNum;

        myRef.child(String.valueOf(quizIdArray[sN][0])).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textView.setText(String.valueOf(dataSnapshot.getValue(String.class)));
                Glide.with(context).using(new FirebaseImageLoader()).load(storageReference.child(String.valueOf(dataSnapshot.getValue(String.class))+ ".jpg")).into(imageView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("connectionLog","Connection failed");
            }
        });

        for (int i = 0; i < level; i++){
            final int finalI = i;
            myRef.child(String.valueOf(quizIdArray[sN][finalI])).addValueEventListener(new ValueEventListener() {
                                                                                      @Override
                                                                                      public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                          buttons[mixedButtonsNum[sN][finalI]].setText(String.valueOf(dataSnapshot.getValue(String.class)));
                                                                                      }

                                                                                      @Override
                                                                                      public void onCancelled(DatabaseError databaseError) {
                                                                                          System.out.println("connection failed");
                                                                                      }
                                                                                  });

        }

    }

}
