package com.c2mtechnology.text2learn.learn;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.c2mtechnology.text2learn.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {
    List<String> words ;
    Context context;
    HashMap<String,EditText> editTexts;
    HashMap<String,TextView> textViews;
    int count = 0;


    int index;

    // flag for footer ProgressBar (i.e. last item of list)
    private boolean isLoadingAdded = false;

    public WordsAdapter(List<String> words, Context context) {
        this.words = words;
        this.context = context;
        editTexts = new HashMap<>();
        textViews = new HashMap<>();
        index = 0;
    }

    public void add(String w) {
        words.add(w);
        notifyItemInserted(words.size() - 1);
    }

    public void addAll(List<String> w) {
        words.clear();
        words.addAll(w);
        notifyDataSetChanged();
    }

    public void remove(String w) {
        int position = words.indexOf(w);
        if (position > -1) {
            words.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public String getItem(int position) {
        return words.get(position);
    }

    @NonNull
    @NotNull
    @Override
    public WordsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.words_layout,parent,false);
        count += 1;
//        Log.i("count : ", count +"");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WordsAdapter.ViewHolder holder, int position) {
        String word = words.get(position);
//        Log.i("pos : ",position + " => word : " + word);

        holder.setData(word,position);

    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView firstLetter;
        EditText remaining_et;
        RelativeLayout rl;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            firstLetter     = itemView.findViewById(R.id.first_letter_tv);
            remaining_et    = itemView.findViewById(R.id.remaining_letter_et);
            rl              = itemView.findViewById(R.id.rl);
        }

        public void setData(String word,int position)
        {
            String pos = String.valueOf(position);
            firstLetter.setTag(position);

            editTexts.put(pos,remaining_et);
            textViews.put(pos,firstLetter);

            word = word.trim();
            int n = word.length() - 1;


            if(word.length() == 1)
            {
                float measureText = remaining_et.getPaint().measureText(word);
//                rl.setMinimumWidth((int) measureText);
                editTexts.get(pos).setWidth((int) measureText);
                textViews.get(pos).setText(""+word.charAt(0));

            }else if(word.length() > 0){

                try{
                    float measureText = editTexts.get(pos).getPaint().measureText(word.substring(1));
                    editTexts.get(pos).setWidth((int) measureText);
                    textViews.get(pos).setText(""+word.charAt(0));


                    editTexts.get(pos).setFilters(new InputFilter[]{new InputFilter.LengthFilter(n)});
                    editTexts.get(pos).setTag(word.substring(1));
                }catch (Exception ignored)
                {
                    Log.i("error : ",ignored.getMessage());
                }


               String finalWord = word;

               editTexts.get(pos).addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                   }

                   @Override
                   public void onTextChanged(CharSequence s, int start, int before, int count) {
                       if(s.length() == n)
                       {
                           if(s.toString().equals(finalWord.substring(1)))
                           {
                               if(editTexts.size() > (index+1) )
                               {
                                   if(editTexts.containsKey(String.valueOf(index+1)))
                                   {
                                       editTexts.get(String.valueOf(index+1)).requestFocus();
                                       index += 1;
                                   }
                               }
                               editTexts.get(pos).setTextColor(context.getResources().getColor(R.color.success));
                           }else{
                               editTexts.get(pos).setTextColor(context.getResources().getColor(R.color.wrong));
                           }
                       }else if(s.length() == 0){

                           editTexts.get(pos).setTextColor(context.getResources().getColor(R.color.black));

                       }
                   }

                   @Override
                   public void afterTextChanged(Editable s) {

                   }
               });
            }

            textViews.get(pos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(editTexts.get(pos).getTag() != null)
                    {
                        editTexts.get(pos).setText(editTexts.get(pos).getTag().toString());
                        editTexts.get(pos).setTextColor(context.getResources().getColor(R.color.success));
                        index -= 1;
                    }
                }
            });
        }

    }
}
