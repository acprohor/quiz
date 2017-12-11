package com.example.acpro.quiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    static List<ItemModel> list;

    FirebaseDatabase database;
    static DatabaseReference myRef;

    @IgnoreExtraProperties
    static class Item implements Serializable {
        public String name;
        public int sc;

        public Item() {
        }

        Item(String name, int sc){
            this.name = name;
            this.sc = sc;
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("items");

        final ListView listView = findViewById(R.id.lvData);
        final TextView textView = findViewById(R.id.textView3);

        final ItemModelAdapter adapter = new ItemModelAdapter(this, initData());
        listView.setAdapter(adapter);

        Query getLeaderList = myRef.orderByChild("score").limitToLast(20);
        getLeaderList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                list.add(dataSnapshot.getValue(ItemModel.class));
                Collections.sort(list, Collections.reverseOrder(ItemModel.COMPARE_BY_SCORE));

                listView.setAdapter(adapter);
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

        Button buttonPlayAgain = findViewById(R.id.button8);

        buttonPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });

    }

    private List<ItemModel> initData(){
        list = new ArrayList<ItemModel>();
        return list;
    }

}
