package com.example.acpro.quiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database;
    static DatabaseReference myRef;
    private static StorageReference storageReference;

    static boolean selectedState[] = new boolean[11]; // состояние вопроса. отвечен или нет.
    static boolean rightAnswerState[] = new boolean[11]; // верно ли отвечен вопрос.
    static int selectedButtonNum[] = new int[11]; // номер кнопки с правильным ответом.
    static int rightAnswerNotSelected[] = new int[11]; // номер кнопки с правильным ответом при неверном ответе.
    static boolean deletedButtons[][] = new boolean[11][6]; // номер удалённой кнопки.
    static boolean hintUsed[] = new boolean[11]; //использована ли подсказка.
    static TabLayout tabLayout;
    static AlertDialog.Builder builder;
    static Intent intentRes;
    static Intent intentStart;

    static String userName = "unknownPlayer";
    static int score = 0;
    static int factor = 1;
    static int progress = 0;

    static int numberOfButtons;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    static QuizCreator quizCreator1 = new QuizCreator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        myRef = database.getReference("items");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        tabLayout = findViewById(R.id.tabLayout);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Intent intent = getIntent();
        userName = intent.getExtras().getString("userName");
        int level = intent.getExtras().getInt("level");

        switch (level){
            case 0: numberOfButtons = 2;
            factor = 1;
            break;
            case 1: numberOfButtons = 4;
            factor = 2;
            break;
            case 2: numberOfButtons = 6;
            factor = 3;
            break;
        }

        quizCreator1.createIdArray(numberOfButtons);

        builder = new AlertDialog.Builder(this);
        intentRes = new Intent(MainActivity.this,ResultsActivity.class);
        intentStart = new Intent(MainActivity.this,StartActivity.class);

        // clear all massifs and counters
        for (int i=0; i<selectedState.length; i++) {
            selectedState[i] = false;
        }
        for (int i=0; i<rightAnswerState.length; i++){
            rightAnswerState[i] = false;
        }
        for (int i=0;i<selectedButtonNum.length;i++ ){
            selectedButtonNum[i] = 0;
        }
        score = 0;
        progress = 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            final TextView textView1 = (TextView) rootView.findViewById(R.id.textView);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            final FloatingActionButton hintFragmentButton = rootView.findViewById(R.id.floatingActionButton2);


            final List<Integer> answerButtons = Arrays.asList(R.id.button, R.id.button2, R.id.button3, R.id.button4, R.id.button11, R.id.button12);

            final Button allButtons[] = new Button[6];


            for (int i = 0; i < allButtons.length; i++){
                allButtons[i] = (Button) rootView.findViewById(answerButtons.get(i));
            }

            switch (numberOfButtons){
                case 2:
                    for (int i = 2; i < allButtons.length; i++){
                        allButtons[i].setVisibility(View.GONE);
                    }
                    break;
                case 4:
                    for (int i = 4; i < allButtons.length; i++){
                        allButtons[i].setVisibility(View.GONE);
                    }
                    break;
            }

            textView.setVisibility(View.INVISIBLE);
            hintFragmentButton.setVisibility(View.GONE);

            final int section = getArguments().getInt(ARG_SECTION_NUMBER);

            quizCreator1.fillButtons(allButtons, textView, imageView, section, numberOfButtons, getContext());

/*

            hintFragmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hintUsed[getArguments().getInt(ARG_SECTION_NUMBER)] = true;
                    Random random = new Random();
                    int ch = numberOfButtons/2;
                    System.out.println("счётчик до входа в цикл" + ch);
                    int mas[] = new int[6];
                    for (int i =0; i<mas.length; i++){
                        mas[i] = -1;
                    }
                    for (int i=0; i<numberOfButtons; i++){
                        int a = random.nextInt(numberOfButtons);
                        for (int j = 0; j<mas.length; j++){
                            while (a == mas[j]){
                                a = random.nextInt(numberOfButtons);
                            }
                        }
                        if (!allButtons[a].getText().equals(textView.getText())){
                            allButtons[a].setVisibility(View.GONE);
                            deletedButtons[getArguments().getInt(ARG_SECTION_NUMBER)][a] = true;
                            mas[i] = a;
                            ch--;
                            System.out.println("удалили кнопку " + a + " уменьшили счётчик " + ch);
                        }
                        if (ch <= 0){
                            hintFragmentButton.setVisibility(View.GONE);
                            System.out.println("Удилили все. Выход из цикла");
                            break;
                        }
                    }
                }
            });

            if (hintUsed[getArguments().getInt(ARG_SECTION_NUMBER)]){
                for (int i = 0; i < numberOfButtons; i++){
                    if (deletedButtons[section][i]){
                        allButtons[i].setVisibility(View.GONE);
                    }
                }
            }
*/


            if (selectedState[getArguments().getInt(ARG_SECTION_NUMBER)]){
                for (int i=0;i<numberOfButtons;i++){
                    allButtons[i].setEnabled(false);
                }

                if (rightAnswerState[getArguments().getInt(ARG_SECTION_NUMBER)]){
                    allButtons[selectedButtonNum[getArguments().getInt(ARG_SECTION_NUMBER)]].getBackground().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);
                }
                else {
                    allButtons[selectedButtonNum[getArguments().getInt(ARG_SECTION_NUMBER)]].getBackground().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.MULTIPLY);
                    allButtons[rightAnswerNotSelected[getArguments().getInt(ARG_SECTION_NUMBER)]].getBackground().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);
                }
            }

            for (int i = 0; i < numberOfButtons; i++) {
                final Button button = allButtons[i];
                final int finalI = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedState[getArguments().getInt(ARG_SECTION_NUMBER)] = true;
                        if (button.getText().equals(textView.getText())) {
                            button.getBackground().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);
                            textView1.setText("+" + (10 * factor) + " points");
                            tabLayout.getTabAt(getArguments().getInt(ARG_SECTION_NUMBER)-1).setIcon(R.drawable.ic_yes);
                            tabLayout.getTabAt(getArguments().getInt(ARG_SECTION_NUMBER)-1).setText("");
                            score += 10 * factor;
                            progress++;
                            for (Button button1:allButtons){
                                button1.setEnabled(false);
                            }
                            rightAnswerState[getArguments().getInt(ARG_SECTION_NUMBER)] = true;
                            selectedButtonNum[getArguments().getInt(ARG_SECTION_NUMBER)] = finalI;
                        } else {
                            button.getBackground().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.MULTIPLY);
                            textView1.setText("wrong");
                            tabLayout.getTabAt(getArguments().getInt(ARG_SECTION_NUMBER)-1).setIcon(R.drawable.ic_no);
                            tabLayout.getTabAt(getArguments().getInt(ARG_SECTION_NUMBER)-1).setText("");
                            progress++;
                            for (Button button1:allButtons){
                                button1.setEnabled(false);
                            }
                            for (int j =0; j < numberOfButtons; j++){  // проверяем остальные кнопки и правильный ответ выделяем зелёным. ИИИ запиешем правильный ответ в очередной массив)
                                if (allButtons[j].getText().equals(textView.getText())){
                                    allButtons[j].getBackground().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);
                                    rightAnswerNotSelected[getArguments().getInt(ARG_SECTION_NUMBER)] = j;
                                    break;
                                }
                            }
                            rightAnswerState[getArguments().getInt(ARG_SECTION_NUMBER)] = false;
                            selectedButtonNum[getArguments().getInt(ARG_SECTION_NUMBER)] = finalI;
                        }
                        if (progress > 9) {
                            // Create dialog.
                            builder.setTitle("results");
                            builder.setMessage("Player " + userName +"\n\n" + "score: " + score );
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (!userName.equals("")) {
                                        ItemModel itemModel = new ItemModel(userName, score);
                                        myRef.push().setValue(itemModel);
                                    }
                                    startActivity(intentStart);
                                }
                            });
                            builder.setNeutralButton("Show table", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (!userName.equals("")) {
                                        ItemModel itemModel = new ItemModel(userName, score);
                                        myRef.push().setValue(itemModel);
                                    }
                                    startActivity(intentRes);
                                }
                        });
                            builder.setCancelable(true);
                            builder.show();
                        }
                    }
                });
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return PlaceholderFragment.newInstance(position + 1);
            //return PlaceholderFragment.newInstance(position + 1,list.get(position));
        }

        @Override
        public int getCount() {
            // Show 10 total pages.
            return 10;
        }
    }
}
