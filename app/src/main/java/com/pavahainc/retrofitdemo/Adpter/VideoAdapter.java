package com.pavahainc.retrofitdemo.Adpter;

import static com.pavahainc.retrofitdemo.R.id;
import static com.pavahainc.retrofitdemo.R.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pavahainc.retrofitdemo.Activity.VideoplayerActivity;
import com.pavahainc.retrofitdemo.Database.DBHelper;
import com.pavahainc.retrofitdemo.Database.DBHelper1;
import com.pavahainc.retrofitdemo.Model.Data;
import com.pavahainc.retrofitdemo.Utils.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.RecyclerViewHolder> {

    Context mcontext;
    ArrayList<Data> courseDataArrayList;

    public VideoAdapter(ArrayList<Data> recyclerDataArrayList, Context mcontext) {
        this.courseDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout.itemview_main_rv, parent, false);
        return new RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Data data = courseDataArrayList.get(position);

        holder.texttitle.setText(data.getTitle());
        holder.textcategory.setText(data.getCategory());
        holder.texViews.setText(Integer.toString(data.getViews()));
        holder.textlikes.setText(Integer.toString(data.getLikes()));

        Glide.with(mcontext)
                .load("http://159.65.146.129/EKYPBDF/f776101c/" + data.getID() + ".webp")
                .into(holder.imageView);

        holder.watchletter.setOnClickListener(view -> {

            Toast.makeText(mcontext, "Add to watch letter", Toast.LENGTH_SHORT).show();
            watchletterdata(courseDataArrayList.get(position));

        });

        if (checkFavoriteItem(data)) {
            holder.like.setVisibility(View.GONE);
            holder.unlike.setVisibility(View.VISIBLE);
            holder.textlikes.setText(Integer.toString(Integer.parseInt(holder.textlikes.getText().toString()) + 1));

        } else {
            holder.like.setVisibility(View.VISIBLE);
            holder.unlike.setVisibility(View.GONE);
        }

        holder.like.setOnClickListener(view -> {

            Toast.makeText(mcontext, "Add to favourite ", Toast.LENGTH_SHORT).show();
            addFavoriteItem(data);

        });

        holder.unlike.setOnClickListener(view -> {

            Toast.makeText(mcontext, "Remove to favourite ", Toast.LENGTH_SHORT).show();
            addFavoriteItem(data);

        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mcontext, VideoplayerActivity.class);
                intent.putExtra("id", data.getID());
                mcontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return courseDataArrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, like, unlike;
        TextView texttitle;
        TextView textcategory;
        TextView texViews;
        TextView textlikes;
        TextView watchletter;

        @SuppressLint({"SetTextI18n", "ResourceType"})
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(id.imageview);
            texttitle = itemView.findViewById(id.texttitle);
            textcategory = itemView.findViewById(id.textcategory);
            texViews = itemView.findViewById(id.texViews);
            textlikes = itemView.findViewById(id.textlikes);
            watchletter = itemView.findViewById(id.watchletter);
            like = itemView.findViewById(id.like);
            unlike = itemView.findViewById(id.unlike);
        }
    }

    @SuppressLint({"WrongConstant", "ShowToast"})
    void watchletterdata(Data item) {
        DBHelper dbHelper = new DBHelper(mcontext);
        boolean isContain = false;

        Cursor cursor = dbHelper.readdata();
        while (cursor.moveToNext()) {
            String id = cursor.getString(1);

            if (id.equals(item.getID())) {
                isContain = true;
                break;
            }
        }

        if (!isContain) {
            dbHelper.insertData(item.getID(), item.getTitle(), item.getCategory(), item.getViews(), item.getLikes());
        } else {
            Toast.makeText(mcontext, "All ready added", 1).show();
        }

    }

    void favdata(Data fav) {
        DBHelper1 dbHelper1 = new DBHelper1(mcontext);

        boolean isContains = false;

        Cursor cursor = dbHelper1.readdata();
        while (cursor.moveToNext()) {
            String id = cursor.getString(1);
            if (id.equals(fav.getID())) {
                isContains = true;
                break;
            }
        }
        if (!isContains) {
            dbHelper1.insertData(fav.getID(), fav.getTitle(), fav.getCategory(), fav.getViews(), fav.getLikes());
        } else {
            Toast.makeText(mcontext, "All ready added", Toast.LENGTH_LONG).show();
        }
    }

    boolean checkFavoriteItem(Data item) {
        String likeListData = Common.getPreferenceString(mcontext, "likeList", "");
        if (!likeListData.isEmpty()) {
            List<String> likeList = Arrays.asList(likeListData.split(","));
            if (likeList.contains(item.getID())) {
                return true;
            }
        }
        return false;
    }

    void addFavoriteItem(Data item) {
        String likeListData = Common.getPreferenceString(mcontext, "likeList", "");
        List<String> likeList = new ArrayList<String>();
        if (!likeListData.isEmpty()) {
            likeList.addAll(Arrays.asList(likeListData.split(",")));
        }
        if (likeList.contains(item.getID())) {
            likeList.remove(item.getID());
            DBHelper1 dbHelper1 = new DBHelper1(mcontext);
            dbHelper1.deleteData(item.getID());

        } else {
            likeList.add(item.getID());
            favdata(item);
        }
        String str = String.join(",", likeList);
        Common.setPreferenceString(mcontext, "likeList", str);
        notifyDataSetChanged();
    }
}

