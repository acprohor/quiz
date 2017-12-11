package com.example.acpro.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TempActivity extends AppCompatActivity {
    static FirebaseDatabase database = FirebaseDatabase.getInstance();
    static DatabaseReference myRef = database.getReference().child("skills");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        TextView textView = findViewById(R.id.textView5);

        List<Integer> id = Arrays.asList(R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6);

        Button buttons[] = new Button[6];

        Button button = findViewById(R.id.button13);

        QuizCreator quizCreator = new QuizCreator();


        for (int i=0; i < buttons.length; i++){
            buttons[i] = findViewById(id.get(i));

        }

        final List<String> names = Arrays.asList(
                "adaptive strike",
                "ancient seal",
                "arcane aura",
                "arcane orb",
                "assassinate",
                "ball lightning",
                "battle cry",
                "bedlam",
                "blade fury",
                "blink",
                "blood rite",
                "bloodlust",
                "borrowed time",
                "boulder smash",
                "brain sap",
                "chakram",
                "chaos bolt",
                "charge of darkness",
                "cold embrace",
                "corrosive haze",
                "corrosive skin",
                "crypt swarm",
                "culling blade",
                "dark pact",
                "darkness",
                "decay",
                "dispersion",
                "disruption",
                "dragon tail",
                "duel",
                "earth spike",
                "earth splitter",
                "earthbind",
                "eclipse",
                "ensnare",
                "ether shock",
                "fervor",
                "fissure",
                "flak cannon",
                "flaming lasso",
                "flux",
                "frost blast",
                "fury swipes",
                "glimpse",
                "global silence",
                "god's strenght",
                "grave chill",
                "greevil's greed",
                "heartstopper aura",
                "howl",
                "ice blast",
                "illuminate",
                "infernal blade",
                "inner beast",
                "inner vitality",
                "jingu mastery",
                "laguna blade",
                "lightning storm",
                "macropyre",
                "mana burn",
                "mana shield",
                "marksmanship",
                "meat hook",
                "meledict",
                "metamorphosis",
                "midnight pulse",
                "mortal strike",
                "nature's attendants",
                "necromastery",
                "nether blast",
                "nimbus",
                "open wounds",
                "overcharge",
                "overgrowth",
                "penitence",
                "phantom strike",
                "phase shift",
                "pit of malice",
                "plasma field",
                "primal split",
                "purifying flames",
                "quas",
                "quill spray",
                "ravage",
                "rearm",
                "refraction",
                "repel",
                "return",
                "rocket flare",
                "sand storm",
                "searing chains",
                "shackleshot",
                "shadow wave",
                "shadow word",
                "shockwave",
                "shuriken toss",
                "sonic wave",
                "spin web",
                "spirit lance",
                "sprout",
                "starstorm",
                "stasis trap",
                "strafe",
                "supernova",
                "surge",
                "swashbuckle",
                "telekinesis",
                "tidebringer",
                "time lapse",
                "time lock",
                "toss",
                "tricks of the trade",
                "venomous gale",
                "walrus punch",
                "wave of terror"
        );


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 115; i++){
                    myRef.child(String.valueOf(i)).setValue(names.get(i));
                }
            }
        });

        Random random = new Random();
        int randomButtonsNum[] = new int[6];
        for (int i = 0; i < randomButtonsNum.length; i++){
            randomButtonsNum[i] = random.nextInt(6);
            if (i > 0 ) {
                for (int j = i-1; j >= 0; j--) {
                    while (randomButtonsNum[i] == randomButtonsNum[j]) {
                        randomButtonsNum[i] = random.nextInt(6);
                        j = i-1;
                    }
                }
            }
        }

        int randomSkillsNum[] = new int[10];
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

        int quizIdArray[][] = new int[10][6];

        for (int i = 0; i < 10; i++) {
            quizIdArray[i][0] = randomSkillsNum[i];
        }

        for (int i = 0; i < 10; i++){
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

        System.out.println("выводим полученную матрицу");
        for (int i=0; i < 10; i++){
            for (int j =0 ; j<6;j++){
                System.out.print(quizIdArray[i][j] + " ");
            }
            System.out.println("-----");
        }

        //quizCreator.setSkillsOnButton(buttons);
        quizCreator.createIdArray(6);
        //quizCreator.fillButtons(buttons, textView,1, 6);
    }
}
