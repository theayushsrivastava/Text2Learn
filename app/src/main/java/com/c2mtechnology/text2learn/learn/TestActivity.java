package com.c2mtechnology.text2learn.learn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.UserDictionary;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.c2mtechnology.text2learn.R;
import com.c2mtechnology.text2learn.classes.SharedPrefManager;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    List<String> words;
    ArrayList<View> views;
    String file_name;
    ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        words = new ArrayList<>();
        Intent intent = getIntent();
        file_name  = intent.getStringExtra("file_name");
        if(file_name == null)
        {
            finish();
        }
        TextView file_name_tv = findViewById(R.id.file_tv);
        file_name_tv.setText(file_name);
        getFile();
        shimmerFrameLayout = findViewById(R.id.sfl);

        FlexboxLayout flexboxLayout = (FlexboxLayout) findViewById(R.id.flexBox);
        flexboxLayout.setFlexDirection(FlexDirection.ROW);
        flexboxLayout.setFlexWrap(FlexWrap.WRAP);


        views = new ArrayList<>();
        Handler handler = new Handler(Looper.getMainLooper());
        Thread t  = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0 ; i < words.size(); i++)
                {
                    View v = getLayoutInflater().inflate(R.layout.words_layout,null);
                    v.setTag(i);
                    views.add(v);
//                    flexboxLayout.addView(v);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0 ; i  < views.size() ; i++)
                        {
                            View x = views.get(i);
                            TextView firstLetter    = (TextView) x.findViewById(R.id.first_letter_tv);
                            EditText remaining_et   = (EditText) x.findViewById(R.id.remaining_letter_et);
                            RelativeLayout rl       = (RelativeLayout) x.findViewById(R.id.rl);
                            String word = words.get(i);
                            int n = word.length() - 1;
                            Log.i("words : ", String.valueOf(word.charAt(0)));

                            if(n == 0)
                            {
                                if(word.equals("~"))
                                {
                                    firstLetter.setText("");
                                    rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                                }else{
//                                    rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    firstLetter.setText(String.valueOf(word.charAt(0)) );
                                }
                            }else{
//                                rl.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                firstLetter.setText(String.valueOf(word.charAt(0)) );
                            }


                            if(word.length() > 1)
                            {
                                float measureText = remaining_et.getPaint().measureText(word.substring(1));
                                remaining_et.setWidth((int) measureText);
                                remaining_et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(n)});
                                remaining_et.setTag(word.substring(1)+" "+i);


                                remaining_et.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if(s.length() == n)
                                        {
                                            if(s.toString().equals(word.substring(1)))
                                            {
                                                String tag = remaining_et.getTag().toString();
                                                String[] tags = tag.split(" ");
                                                int index = Integer.parseInt(tags[1]) + 1;
                                                if(index < views.size())
                                                {
                                                    views.get(index).findViewById(R.id.remaining_letter_et).requestFocus();
                                                }

                                                remaining_et.setTextColor(getResources().getColor(R.color.success));
                                            }else{
                                                remaining_et.setTextColor(getResources().getColor(R.color.wrong));
                                            }
                                        }else if(s.length() == 0){

                                            remaining_et.setTextColor(getResources().getColor(R.color.black));

                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                                firstLetter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if(remaining_et.getTag() != null)
                                        {
                                            String tag = remaining_et.getTag().toString();
                                            String[] tags = tag.split(" ");
                                            remaining_et.setText(tags[0]);
                                            int index = Integer.parseInt(tags[1]) + 1;
                                            if(index < views.size())
                                            {
                                                views.get(index).findViewById(R.id.remaining_letter_et).requestFocus();
                                            }

                                            remaining_et.setTextColor(getResources().getColor(R.color.success));
                                        }
                                    }
                                });
                            }
                            flexboxLayout.addView(views.get(i),i);
                        }

                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                });

            }
        });
        t.start();

//        View view = flexboxLayout.getChildAt(0);

//        view.setLayoutParams(lp);
    }



    public void buttonOperations(View v)
    {
        switch (v.getTag().toString())
        {
            case "back":
                finish();
                break;
            case "showAll":
                showAll();
                break;
            case "clear":
                clearAll();
                break;
            case "showWord":
                showWord();
                break;
        }
    }

    public void showWord()
    {
        View v = getCurrentFocus();
        if(v != null && v.getTag() != null)
        {
            String tag = v.getTag().toString();

            if(tag != null && v instanceof EditText)
            {

                String[] tags = tag.split(" ");
                ((EditText) v).setText(tags[0]);
                int index = Integer.parseInt(tags[1]) + 1;
                while (true)
                {
                    if(index < views.size())
                    {
                        if(views.get(index).findViewById(R.id.remaining_letter_et).requestFocus())
                        {
                            break;
                        }
                    }else{
                        break;
                    }
                    index += 1;
                }
            }
            Log.i("index : ",tag);
        }
    }

    public void showAll()
    {
        for(int i = 0 ; i < views.size() ; i++)
        {
            View v = views.get(i);
            EditText et = v.findViewById(R.id.remaining_letter_et);
            String word = words.get(i);
            if(word.length() > 1)
            {
                et.setText(word.substring(1));
                et.setTextColor(getResources().getColor(R.color.success));
            }
        }
    }

    public void clearAll()
    {
        if(views.size() > 0)
        {
            for(int i = 0 ; i < views.size() ; i++)
            {
                View v = views.get(i);
                EditText et = v.findViewById(R.id.remaining_letter_et);
                String word = words.get(i);
                if(word.length() > 1)
                {
                    et.setText("");
                    et.setTextColor(getResources().getColor(R.color.black));
                }
            }

            views.get(0).findViewById(R.id.remaining_letter_et).requestFocus();
        }
    }



    private void getFile()
    {
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        String file = sharedPrefManager.getFile(file_name);
        file = file.replace("\n", "  ~ ");
        Log.i("text",file);
        String[] words1  = file.split("(?<=[,.])|(?=[,.])|\\s+");
//        String[] d = file.split("\\b");
        int count = 0;
        for(int i = 0 ; i  < words1.length ; i++)
        {

            words1[i] = words1[i].trim();
            if(!words1[i].equals(""))
            {
                words.add(count,words1[i]);
                count += 1;
            }
        }
        Log.i("file1 : ",words.toString()) ;
    }

}