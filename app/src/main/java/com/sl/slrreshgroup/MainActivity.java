package com.sl.slrreshgroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sl.slrefreshgroup.view.IRefreshView;
import com.sl.slrefreshgroup.view.SlRefreshGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SlRefreshGroup refresh_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv.setAdapter(new ItemAdapter());
         refresh_view = findViewById(R.id.content);
        refresh_view.setOnRefreshListener(new IRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refresh_view.finishRefresh();
                                }
                            });

                        } catch (InterruptedException e) {
                            e.printStackTrace();
//                            refresh_view.finishRefresh();
                        }
                    }
                }).start();
            }
        });
    }

    public class ItemAdapter extends RecyclerView.Adapter{


        private List mData;
        private Map<String ,Integer> typeMap=new HashMap();

        public void setmData(List mData) {
            this.mData = mData;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_header, null);
            return new Holder(inflate);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }


        @Override
        public int getItemCount() {
            return 20;
        }


        public  class Holder extends RecyclerView.ViewHolder{


            public Holder(View itemView) {
                super(itemView);

            }
        }
    }
}
