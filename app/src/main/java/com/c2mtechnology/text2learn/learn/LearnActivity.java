package com.c2mtechnology.text2learn.learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.c2mtechnology.text2learn.R;
import com.c2mtechnology.text2learn.classes.SharedPrefManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static android.view.Gravity.FILL_HORIZONTAL;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.VERTICAL;

public class LearnActivity extends AppCompatActivity {
   List<String> words;
   ArrayList<Integer> startIndexes;
   RecyclerView recyclerView ;
   WordsAdapter adapter;

   HashMap<Integer,Integer> pagination;

   String file_name = null;
   int start_index = 0,end_index = 0,current_index = 0;

    FlexboxLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        words = new ArrayList<>();
        pagination = new HashMap<>();
        startIndexes = new ArrayList<>();

        recyclerView = findViewById(R.id.rv);
        Intent intent = getIntent();
        file_name  = intent.getStringExtra("file_name");
        if(file_name == null)
        {
            finish();
        }

        layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
//        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        recyclerView.setLayoutManager(layoutManager);

        init();
        compute();
        getFile();
    }

    public void init()
    {
        adapter = new WordsAdapter(words,this);
        recyclerView.setAdapter(adapter);
        startIndexes.add(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int childCount = layoutManager.getChildCount();
                if( childCount > words.size() )
                {
                    pagination.put(0,words.size());
                    current_index = 0;
                }else{
                    pagination.put(0,childCount);
                    current_index = 0;
                }
                adapter = new WordsAdapter(words.subList(current_index,pagination.get(current_index)), LearnActivity.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
                Log.i("total child count : ",childCount + "");
                Log.i("total : ",words.size() + "");
            }
        }, 500);
    }

    public void compute()
    {

    }



    private void getFile()
    {
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        String file = sharedPrefManager.getFile(file_name);
        String[] fileContents  = file.split("(?<=[,.])|(?=[,.])|\\s+");
//        String[] d = file.split("\\b");
        words.addAll(Arrays.asList(fileContents));
        adapter.notifyDataSetChanged();
        Log.i("file1 : ",words.toString()) ;
    }



}