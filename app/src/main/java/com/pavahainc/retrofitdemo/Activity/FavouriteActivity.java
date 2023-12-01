package com.pavahainc.retrofitdemo.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavahainc.retrofitdemo.Adpter.FavouriteAdapter;
import com.pavahainc.retrofitdemo.Database.DBHelper1;
import com.pavahainc.retrofitdemo.Model.Data;
import com.pavahainc.retrofitdemo.R;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {

    private TextView noDataFoundTxt;
    private RecyclerView favouriteRV;
    FavouriteAdapter favouriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        noDataFoundTxt = (TextView) findViewById(R.id.noDataFoundTxt);
        favouriteRV = (RecyclerView) findViewById(R.id.favouriteRV);

        favouriteData();
    }

    void favouriteData() {
        DBHelper1 dbHelper1 = new DBHelper1(FavouriteActivity.this);
        Cursor cursor = dbHelper1.readdata();

        ArrayList<Data> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(1);
            String title = cursor.getString(2);
            String category = cursor.getString(3);
            int views = cursor.getInt(4);
            int likes = cursor.getInt(5);

            LinearLayoutManager manager = new LinearLayoutManager(FavouriteActivity.this);
            manager.setReverseLayout(true);
            manager.setStackFromEnd(true);

            favouriteRV.setLayoutManager(manager);
            Data dat = new Data(id, title, category, views, likes);
            arrayList.add(dat);
        }

        if (arrayList.size() > 0) {
            favouriteAdapter = new FavouriteAdapter(arrayList, this, pos -> {
                if (arrayList.size() == 0) {
                    noDataFoundTxt.setVisibility(View.VISIBLE);
                } else {
                    noDataFoundTxt.setVisibility(View.GONE);
                }
            });
            favouriteRV.setAdapter(favouriteAdapter);
        } else {
            noDataFoundTxt.setVisibility(View.VISIBLE);
        }

    }
}