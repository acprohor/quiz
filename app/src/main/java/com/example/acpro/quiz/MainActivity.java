package com.example.acpro.quiz;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static boolean selectedState[] = new boolean[6]; // состояние вопроса. отвечен или нет.
    static boolean rightAnswerState[] = new boolean[6]; // верно ли отвечен вопрос.
    static int selectedButtonNum[] = new int[6]; // номер кнопки с правильным ответом
    //static int imageNames[] = new int[6];
    int mas[] = new int[6];
    //static List<Integer> test = Arrays.asList(R.drawable.moon_shard, R.drawable.one, R.drawable.two, R.drawable.three);
    static List<String> imageNames = Arrays.asList("one.jpg","two.jpg","three.jpg","four.jpg","clock_ability3.jpg","abaddona_ability4.jpg",
            "chaos_ability1");



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

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        quizCreator1.createQuizArray();
        quizCreator1.createNewSomething();
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
            //args.putli
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            final TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            final TextView textView1 = (TextView) rootView.findViewById(R.id.textView);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);

            //String anotherRightAnswer = "nothing";
            String anotherQuestion = "nothing";

            ArrayList<String> anotherNewQuiz = new ArrayList<>();
            anotherNewQuiz = quizCreator1.quizArray.get(getArguments().getInt(ARG_SECTION_NUMBER));
            anotherQuestion = anotherNewQuiz.get(0);
            final String anotherRightAnswer = anotherNewQuiz.get(1);
            final String choose;
            final List<Integer> answerButtons = Arrays.asList(R.id.button, R.id.button2, R.id.button3, R.id.button4);

            final Button allButtons[] = new Button[4];

            for (int i=0; i<4;i++){
                allButtons[i] = (Button) rootView.findViewById(answerButtons.get(i));
            }

            // Set Choices.
            for (int i = 0; i <4; i++){
                allButtons[i].setText(anotherNewQuiz.get(i+2));
            }

            if (selectedState[getArguments().getInt(ARG_SECTION_NUMBER)]){
                System.out.println("На этот вопрос уже был дан ответ.");
                for (int i=0;i<4;i++){
                    allButtons[i].setEnabled(false);
                }

                System.out.println("Выключили кнопки");
                if (rightAnswerState[getArguments().getInt(ARG_SECTION_NUMBER)]){
                    System.out.println("Ответ был дан верный, значит выделяем его зелёным.");
                    allButtons[selectedButtonNum[getArguments().getInt(ARG_SECTION_NUMBER)]].getBackground().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);
                }
                else {
                    System.out.println("Ответ был дан не верно, выделяем красным вариант, и правильный ответ");
                    allButtons[selectedButtonNum[getArguments().getInt(ARG_SECTION_NUMBER)]].getBackground().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.MULTIPLY);
                    System.out.println("Начинаем искать правильный ответ.");
                    for (int j=0;j<4;j++){
                        System.out.println("Текст из кнопки: " + allButtons[j].getText() + " сравним с правильным ответом: " + anotherRightAnswer);
                        Button buttonTemp = allButtons[j];
                        if (buttonTemp.getText().equals(anotherRightAnswer)){
                            System.out.println("Нашли кнопку с правильным ответом, выделим её зелёным.");
                            allButtons[j].getBackground().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);
                            break;
                        }
                    }
                }
            }

            textView1.setText(anotherQuestion);
            //imageView.setImageDrawable(Drawable.createFromPath("C:\\Users\\acpro\\AndroidStudioProjects\\quiz\\app\\src\\main\\res\\drawable\\moon_shard.jpg"));
            //imageView.setImageDrawable(getResources().getDrawable(R.drawable.moon_shard));
            //imageView.setImageDrawable(getResources().getDrawable(test.get(getArguments().getInt(ARG_SECTION_NUMBER))));
            //imageView.setImageResource(R.drawable.);

            AssetManager assets = getActivity().getAssets();
            String answerStringOfPath = imageNames.get(getArguments().getInt(ARG_SECTION_NUMBER));
            try {
                imageView.setImageDrawable(Drawable.createFromStream(assets.open(answerStringOfPath), ""));
            } catch (IOException e) {
                e.printStackTrace();
            }


            for (int i = 0; i < allButtons.length; i++) {
                final Button button = allButtons[i];
                final int finalI = i;
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedState[getArguments().getInt(ARG_SECTION_NUMBER)] = true;
                        if (button.getText().equals(anotherRightAnswer)) {
                            button.getBackground().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);
                            textView1.setText("Nice, good job");
                            for (Button button1:allButtons){
                                button1.setEnabled(false);
                            }
                            rightAnswerState[getArguments().getInt(ARG_SECTION_NUMBER)] = true;
                            selectedButtonNum[getArguments().getInt(ARG_SECTION_NUMBER)] = finalI;
                        } else {
                            button.getBackground().setColorFilter(Color.parseColor("#F44336"), PorterDuff.Mode.MULTIPLY);
                            textView1.setText("No :( Right answer is " + anotherRightAnswer);
                            for (Button button1:allButtons){
                                button1.setEnabled(false);
                            }
                            for (int j =0; j < allButtons.length; j++){  // проверяем остальные кнопки и правильный ответ выделяем зелёным.
                                if (allButtons[j].getText().equals(anotherRightAnswer)){
                                    allButtons[j].getBackground().setColorFilter(Color.parseColor("#4CAF50"), PorterDuff.Mode.MULTIPLY);
                                }
                            }
                            rightAnswerState[getArguments().getInt(ARG_SECTION_NUMBER)] = false;
                            selectedButtonNum[getArguments().getInt(ARG_SECTION_NUMBER)] = finalI; //  переписать чтобы использовалось один раз вне оператора if
                        }

                    }
                });
            }

            textView.setText(anotherRightAnswer);
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
            // Show 5 total pages.
            return 5;
        }
    }
}
