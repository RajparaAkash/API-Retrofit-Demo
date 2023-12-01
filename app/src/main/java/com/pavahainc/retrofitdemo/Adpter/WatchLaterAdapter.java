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
import com.pavahainc.retrofitdemo.Database.DBHelper;
import com.pavahainc.retrofitdemo.Model.Data;
import com.pavahainc.retrofitdemo.R;

import java.util.ArrayList;

public class WatchLaterAdapter extends RecyclerView.Adapter<WatchLaterAdapter.RecyclerViewHolder> {

    Context context;
    ArrayList<Data> arrayList;
    private Clickinterface clickinterface;

    public WatchLaterAdapter(ArrayList<Data> arrayList, Context context, Clickinterface clickinterface) {
        this.arrayList = arrayList;
        this.context = context;
        this.clickinterface = clickinterface;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_watch_later, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Data data = arrayList.get(position);

        holder.texttitle.setText(data.getTitle());
        holder.textcategory.setText(data.getCategory());
        holder.texViews.setText(Integer.toString(data.getViews()));
        holder.textlikes.setText(Integer.toString(data.getLikes()));

        Glide.with(context)
                .load("http://159.65.146.129/EKYPBDF/f776101c/" + data.getID() + ".webp")
                .into(holder.imageView);

        DBHelper dbHelper = new DBHelper(context);

        holder.delete.setOnClickListener(view -> {
            dbHelper.deleteData(data.getID());
            arrayList.remove(arrayList.get(position));
            notifyDataSetChanged();
            clickinterface.onClick(arrayList.size());
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
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

        }
    }

    public interface Clickinterface {
        void onClick(int pos);
    }
}
