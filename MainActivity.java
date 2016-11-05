package com.example.kellen2086189370.multithreading;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final int PROGRESS = 0x1;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    private Handler mHandler;

    public String filename = "numbers.txt";
    public List<String> numList = new ArrayList<String>();
    public ArrayAdapter adapter;
    public FileOutputStream outputStream;
    public OutputStreamWriter outputWriter;
    public FileInputStream fIn;
    public InputStreamReader isr;
    public BufferedReader buffreader;
    public ListView listView;

    public void writeFile(){
        try {
            System.out.println("writing");
            for (int i = 1; i <= 10; i++) {
                outputWriter.write(Integer.toString(i));
                outputWriter.write("\n");
                Thread.sleep(250);
                System.out.println(i);
            }
            outputWriter.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void create(View view){
            System.out.println("creating");
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try{
                        System.out.println("running");
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputWriter = new OutputStreamWriter(outputStream);
                        writeFile();
                    }catch (Throwable e){
                        e.printStackTrace();
                    }
                }
            });
        thread.start();
    }

    public void readFile(){
        try {
            fIn = openFileInput ( filename ) ;
            isr = new InputStreamReader ( fIn ) ;
            buffreader = new BufferedReader ( isr ) ;
            String readString = buffreader.readLine();
            while (readString != null) {
                numList.add(readString);
                Thread.sleep(250);
                System.out.println(readString);
                readString = buffreader.readLine();
            }
            isr.close();
        }catch (Throwable e){
            e.printStackTrace();
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                listView.setAdapter(adapter);

            }
        });
    }

    public void load(View view){
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        readFile();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        thread.start();
        adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,numList);
        listView = (ListView) findViewById(R.id.listView);
    }

    public void clear(View view){
        adapter.clear();
    }

    public int checkProgress(){
        if (numList.size()<10){
            return (numList.size() * 5);
        }
        else{
            return 0;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        mHandler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    mProgressStatus = checkProgress();

                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        }).start();
    }
}
