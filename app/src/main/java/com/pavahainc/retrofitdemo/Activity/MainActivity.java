package com.pavahainc.retrofitdemo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavahainc.retrofitdemo.Adpter.VideoAdapter;
import com.pavahainc.retrofitdemo.Api.RetrofitAPI;
import com.pavahainc.retrofitdemo.Model.Data;
import com.pavahainc.retrofitdemo.Model.RecyclerData;
import com.pavahainc.retrofitdemo.R;
import com.pavahainc.retrofitdemo.Utils.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainListRV;
    private ProgressBar progressBar;
    private ArrayList<Data> recyclerDataArrayList;
    private VideoAdapter videoAdapter;
    int totalpages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainListRV = findViewById(R.id.mainListRV);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.favouriteBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FavouriteActivity.class));
            }
        });

        findViewById(R.id.watchLaterBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WatchLetterActivity.class));
            }
        });

        recyclerDataArrayList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);

        mainListRV.setLayoutManager(layoutManager);
        videoAdapter = new VideoAdapter(recyclerDataArrayList, MainActivity.this);
        mainListRV.setAdapter(videoAdapter);

        mainListRV.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onFirstVisibleItem(int i) {
            }

            @Override
            public void onLoadMore(int i) {

                Log.e("TAG", "i: " + i);
                Log.e("TAG", "totalpages: " + totalpages);

                if (totalpages >= i) {
                    getAllCourses(i);
                }
            }
        });

        getAllCourses(1);

    }

    private void getAllCourses(int i) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://159.65.146.129/api/videos/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<RecyclerData> call = retrofitAPI.getAllCourses(i);

        call.enqueue(new Callback<RecyclerData>() {
            @Override
            public void onResponse(@NonNull Call<RecyclerData> call, @NonNull Response<RecyclerData> response) {

                Log.e("TAG", "onResponse: " + response);

                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    assert response.body() != null;
                    ArrayList<Data> templist = (ArrayList<Data>) response.body().getDataArray();
                    recyclerDataArrayList.addAll(templist);
                    videoAdapter.notifyDataSetChanged();
                    totalpages = response.body().getTotalpages();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecyclerData> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoAdapter.notifyDataSetChanged();
    }
}