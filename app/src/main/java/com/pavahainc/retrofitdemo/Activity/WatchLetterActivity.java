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
import com.pavahainc.retrofitdemo.Adpter.WatchLaterAdapter;
import com.pavahainc.retrofitdemo.Database.DBHelper;
import com.pavahainc.retrofitdemo.Model.Data;
import com.pavahainc.retrofitdemo.R;

import java.util.ArrayList;

public class WatchLetterActivity extends AppCompatActivity {

    private TextView noDataFoundTxt;
    private RecyclerView watchLaterRV;
    WatchLaterAdapter watchLaterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_letter);

        noDataFoundTxt = (TextView) findViewById(R.id.noDataFoundTxt);
        watchLaterRV = (RecyclerView) findViewById(R.id.watchLaterRV);

        watchletterdata();
    }

    void watchletterdata() {
        DBHelper dbHelper = new DBHelper(WatchLetterActivity.this);
        Cursor cursor = dbHelper.readdata();

        ArrayList<Data> arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(1);
            String title = cursor.getString(2);
            String category = cursor.getString(3);
            int views = cursor.getInt(4);
            int likes = cursor.getInt(5);

            LinearLayoutManager manager = new LinearLayoutManager(WatchLetterActivity.this);

            watchLaterRV.setLayoutManager(manager);
            Data data = new Data(id, title, category, views, likes);
            arrayList.add(data);

        }

        if (arrayList.size() > 0) {
            watchLaterAdapter = new WatchLaterAdapter(arrayList, this, pos -> {
                if (arrayList.size() == 0) {
                    noDataFoundTxt.setVisibility(View.VISIBLE);
                } else {
                    noDataFoundTxt.setVisibility(View.GONE);
                }
            });
            watchLaterRV.setAdapter(watchLaterAdapter);
        } else {
            noDataFoundTxt.setVisibility(View.VISIBLE);
        }
    }
}