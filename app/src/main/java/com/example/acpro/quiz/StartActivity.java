package com.example.acpro.quiz;

import android.app.DownloadManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final QuizCreator quizCreator = new QuizCreator();


        boolean ready = quizCreator.ready;
        final EditText editText = findViewById(R.id.editText);
        Button startGame = findViewById(R.id.button5);
        Button buttonShowResults = findViewById(R.id.button9);
        TextView textViewLevel = findViewById(R.id.textViewLevel);
        Button buttonCheck = findViewById(R.id.button10);

        final SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);


        final Intent intent = new Intent(StartActivity.this, MainActivity.class);


        final String allQuestions[];
        allQuestions = quizCreator.getSkills(startGame);
        buttonCheck.setText(allQuestions[3]);
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(StartActivity.this, TempActivity.class);
                startActivity(intent1);

                /*for (int i = 0; i < 7; i++){
                    System.out.println(allQuestions[i]);
                }*/
            }
        });

        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("userName", editText.getText().toString());
                int progress = seekBar.getProgress();
                intent.putExtra("level", progress);
                intent.putExtra("abilities" , quizCreator.temp);

                startActivity(intent);
            }
        });

        buttonShowResults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(StartActivity.this, ResultsActivity.class);
                startActivity(intent1);
            }
        });

    }
}
