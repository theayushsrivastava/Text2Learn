package com.c2mtechnology.text2learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.c2mtechnology.text2learn.classes.AllFIlesAdapter;
import com.c2mtechnology.text2learn.classes.SharedPrefManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;

public class AllFilesActivity extends AppCompatActivity implements AllFIlesAdapter.Callback{

    SharedPrefManager sharedPrefManager;
    HashMap<String,String> files;
    RecyclerView rv;
    AllFIlesAdapter adapter;
    RelativeLayout rl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_files);
        rl = findViewById(R.id.rl1);
        rv = findViewById(R.id.rv1);

        sharedPrefManager = SharedPrefManager.getInstance(this);
        files = sharedPrefManager.getAllFiles();
        if(files == null)
        {
            rv.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
            Toast.makeText(this,"No files present ! create now",Toast.LENGTH_SHORT).show();
        }else{
            rv.setVisibility(View.VISIBLE);
            adapter = new AllFIlesAdapter(files,this,AllFilesActivity.this);
            rv.setAdapter(adapter);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);


    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList()
    {

        files = sharedPrefManager.getAllFiles();
        if(files == null || files.size() == 0)
        {
            rv.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
        }else{
            rv.setVisibility(View.VISIBLE);
            rl.setVisibility(View.GONE);
            adapter = new AllFIlesAdapter(files,this,AllFilesActivity.this);
            rv.setAdapter(adapter);
        }


    }

    public void buttonOperations(View v)
    {
        switch (v.getTag().toString())
        {
            case "back":
                finish();
                break;
            case "new":
                Intent intent = new Intent(this,MakeFileActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void delete(String name) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setMessage("Delete File : " + name + " ?");



        builder.setTitle("Delete File")
                .setIcon(R.drawable.ic_save)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if(sharedPrefManager.deleteFile(name))
                       {
                           Toast.makeText(AllFilesActivity.this,"File deleted !",Toast.LENGTH_SHORT).show();
                       }
                       refreshList();
                    }
                });
        builder.setNegativeButton("No",null);
        builder.show();

    }
}