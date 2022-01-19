package com.c2mtechnology.text2learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.c2mtechnology.text2learn.learn.LearnActivity;
import com.c2mtechnology.text2learn.learn.TestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void buttonOperations(View v)
    {
        String tag = v.getTag().toString();
        switch (tag){
            case "new":
                Intent intent  = new Intent(this,MakeFileActivity.class);
                startActivity(intent);
                break;
            case "open":
                Intent intent1  = new Intent(this, AllFilesActivity.class);
                startActivity(intent1);
                break;
        }
    }
}