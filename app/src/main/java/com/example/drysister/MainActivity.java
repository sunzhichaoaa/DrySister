package com.example.drysister;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.drysister.Bean.Sister;
import com.example.drysister.network.SisterApi;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button showBtn;
    private ImageView showImg;
    private Button refreshBtn;

    private ArrayList<Sister> data;
    private int curPos = 0;
    private int page = 1;
    private PictureLoader loader;
    private SisterApi sisterApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loader = new PictureLoader();
        sisterApi = new SisterApi();
        initData();
        showBtn = (Button) findViewById(R.id.next_sister);
        showImg = (ImageView) findViewById(R.id.img);
        refreshBtn = findViewById(R.id.next_page);
        showBtn.setOnClickListener(this);
        refreshBtn.setOnClickListener(this);

    }
    private void initData() {
        data = new ArrayList<>();
        new SisterTask(page).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_sister:
                if (curPos > 9) {
                    curPos = 0;
                }
                Log.i("好好学习2", "onClick: " + data.get(curPos).getUrl());
                loader.load(showImg, data.get(curPos).getUrl());
                curPos++;
                break;
            case R.id.next_page:
                page++;
                new SisterTask(page).execute();
                curPos = 0;
                break;
            default:
                break;
        }
    }
    private class SisterTask extends AsyncTask<Void,Void,ArrayList<Sister>>{
    private int page;
    public SisterTask(int page){
        this.page = page;
    }

        @Override
        protected ArrayList<Sister> doInBackground(Void... voids) {
            return sisterApi.fetchSister(10,page);
        }

        @Override
        protected void onPostExecute(ArrayList<Sister> sisters) {
            super.onPostExecute(sisters);
            data.clear();
            data.addAll(sisters);
        }
    }
}
