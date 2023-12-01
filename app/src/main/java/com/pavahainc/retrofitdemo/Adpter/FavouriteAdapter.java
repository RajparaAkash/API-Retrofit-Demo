package com.pavahainc.retrofitdemo.Adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pavahainc.retrofitdemo.Database.DBHelper1;
import com.pavahainc.retrofitdemo.Model.Data;
import com.pavahainc.retrofitdemo.R;
import com.pavahainc.retrofitdemo.Utils.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.RecyclerViewHolder> {

    Context contextt;
    ArrayList<Data> arrayList1;
    private Clickinterface clickinterface;

    public FavouriteAdapter(ArrayList<Data> arrayList1, Context contextt, Clickinterface clickinterface) {
        this.arrayList1 = arrayList1;
        this.contextt = contextt;
        this.clickinterface = clickinterface;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_favourite_video, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Data dat = arrayList1.get(position);

        holder.texttitle.setText(dat.getTitle());
        holder.textcategory.setText(dat.getCategory());
        holder.texViews.setText(Integer.toString(dat.getViews()));

        holder.textlikes.setText(Integer.toString(dat.getLikes() + 1));

        Glide.with(contextt)
                .load("http://159.65.146.129/EKYPBDF/f776101c/" + dat.getID() + ".webp")
                .into(holder.imageView);

        DBHelper1 dbHelper1 = new DBHelper1(contextt);

        holder.delete.setOnClickListener(view -> {
            addFavoriteItem(dat);
            dbHelper1.deleteData(dat.getID());
            arrayList1.remove(arrayList1.get(position));
            notifyDataSetChanged();
            clickinterface.onClick(arrayList1.size());
        });

        holder.like.setOnClickListener(view -> {
            addFavoriteItem(dat);
            dbHelper1.deleteData(dat.getID());
            arrayList1.remove(arrayList1.get(position));
            notifyDataSetChanged();
            clickinterface.onClick(arrayList1.size());
        });
    }

    @Override
    public int getItemCount() {
        return arrayList1.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, like;
        TextView texttitle;
        TextView textcategory;
        TextView texViews;
        TextView textlikes;
        TextView delete;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            texttitle = itemView.findViewById(R.id.texttitle);
            textcategory = itemView.findViewById(R.id.textcategory);
            texViews = itemView.findViewById(R.id.texViews);
            textlikes = itemView.findViewById(R.id.textlikes);
            delete = itemView.findViewById(R.id.delete);
            like = itemView.findViewById(R.id.like);
        }
    }

    void addFavoriteItem(Data item) {
        String likeListData = Common.getPreferenceString(contextt, "likeList", "");
        List<String> likeList = new ArrayList<String>();
        if (!likeListData.isEmpty()) {
            likeList.addAll(Arrays.asList(likeListData.split(",")));
        }
        if (likeList.contains(item.getID())) {
            likeList.remove(item.getID());
            DBHelper1 dbHelper1 = new DBHelper1(contextt);
            dbHelper1.deleteData(item.getID());

        } else {
            likeList.add(item.getID());
        }
        String str = String.join(",", likeList);
        Common.setPreferenceString(contextt, "likeList", str);
        notifyDataSetChanged();
    }

    public interface Clickinterface {
        void onClick(int pos);
    }
}
