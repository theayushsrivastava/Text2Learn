package com.c2mtechnology.text2learn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.c2mtechnology.text2learn.classes.SharedPrefManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.HashMap;

public class MakeFileActivity extends AppCompatActivity {

    String file_name = null;
    TextView file_content_tv;
    TextView save_button;
    AlertDialog alertDialog;
    HashMap<String,String> files;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_file);
        Intent intent = getIntent();
        file_name = intent.getStringExtra("file_name");

        file_content_tv = findViewById(R.id.file_content_tv);
        save_button     = findViewById(R.id.save);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile();
            }
        });

        sharedPrefManager     = SharedPrefManager.getInstance(this);
        files            = sharedPrefManager.getAllFiles();
        if(files == null)
        {
            files = new HashMap<>();
        }

        if(file_name != null)
        {
            file_content_tv.setText(files.get(file_name));
            files.remove(file_name);
        }
    }

    public void buttonOperations(View v)
    {
        String tag = v.getTag().toString();
        switch (tag){
            case "back":
                finish();
                break;
            case "save":
                saveFile();
                break;
        }
    }


    private void saveFile()
    {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setMessage("Enter file name : ");

        final EditText input = new EditText(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);
        input.requestFocus();
        if(file_name != null)
        {
            input.setText(file_name);
        }

        builder.setTitle("Save File")
                .setIcon(R.drawable.ic_save)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String filename = input.getText().toString();
                        if(filename.equals(""))
                        {
                            Toast.makeText(MakeFileActivity.this,"Enter file name",Toast.LENGTH_SHORT).show();
                        }else{
                            processSaveFile(filename);
                        }
                    }
                });
        builder.setNegativeButton("cancel",null);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void processSaveFile(String fileName)
    {
        if(files.containsKey(fileName))
        {
            Toast.makeText(this,"Filename already exists.",Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson = new Gson();
        files.put(fileName,file_content_tv.getText().toString());
        sharedPrefManager.addFiles(gson.toJson(files));
        Toast.makeText(this,"File saved successfully !",Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
//        finish();
        Log.i("files : ",sharedPrefManager.getAllFiles().toString());
    }
}