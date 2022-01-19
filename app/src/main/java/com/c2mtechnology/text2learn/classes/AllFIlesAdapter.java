package com.c2mtechnology.text2learn.classes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.c2mtechnology.text2learn.MakeFileActivity;
import com.c2mtechnology.text2learn.R;
import com.c2mtechnology.text2learn.learn.TestActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class AllFIlesAdapter extends RecyclerView.Adapter<AllFIlesAdapter.ViewHolder> {
    public interface Callback{
        void delete(String file_name);
    }
    HashMap<String,String> files;
    ArrayList<String> file_names;
    Context context;
    Callback callback;

    public AllFIlesAdapter(HashMap<String, String> files, Context context,Callback callback) {
        this.files = files;
        file_names = new ArrayList<String>(files.keySet());
        this.context = context;
        this.callback = callback;
    }

    @NonNull
    @NotNull
    @Override
    public AllFIlesAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.file_item_layout,parent,false);
        return new AllFIlesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AllFIlesAdapter.ViewHolder holder, int position) {
        holder.setData(file_names.get(position));
    }

    @Override
    public int getItemCount() {
        return file_names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, fileContent;
        ImageButton menu;
        RelativeLayout rl;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title   = itemView.findViewById(R.id.title_tv);
            fileContent = itemView.findViewById(R.id.content_tv);
            menu = itemView.findViewById(R.id.menu_ib);
            rl = itemView.findViewById(R.id.rl);

        }

        public void setData(String t)
        {
            title.setText(t);
            fileContent.setText(files.get(t));
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TestActivity.class);
                    intent.putExtra("file_name",t);
                    context.startActivity(intent);
                }
            });

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBottomSheetDialog(t);
                }
            });
        }

        private void showBottomSheetDialog(String t) {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_layout);
            TextView file_name_tv = bottomSheetDialog.findViewById(R.id.file_tv);
            Button edit             = bottomSheetDialog.findViewById(R.id.edit_button);
            Button delete           = bottomSheetDialog.findViewById(R.id.delete_button);
            assert file_name_tv != null;
            file_name_tv.setText(t);
            assert edit != null;
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetDialog.dismiss();
                    Intent intent = new Intent(context, MakeFileActivity.class);
                    intent.putExtra("file_name",t);
                    context.startActivity(intent);

                }
            });

            assert delete != null;
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.delete(t);
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog.show();
        }
    }
}
