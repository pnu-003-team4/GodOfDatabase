package com.pnu.godofdatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.goddb.DB;
import com.goddb.GodDB;
import com.goddb.GoddbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class god_performance extends AppCompatActivity {

    DB godDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god_performance);


        try {
            godDB = new GodDB.Builder(this)
                    .name("godDB1")
                    .build();
        } catch (GoddbException e) {

        }

        Button god_put_test = findViewById(R.id.god_put_test);
        Button god_get_test = findViewById(R.id.god_get_test);
        final TextView textView = findViewById(R.id.god_output);

        //put test
        god_put_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long beginWriteStr = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    JSONObject jsonObject = new JSONObject();
                    String random_path = Double.toString(Math.random() * 1000);
                    String random_obj = Double.toString(Math.random() * 1000);
                    try {
                        jsonObject.put("val", random_obj);
                        godDB.put(random_path, jsonObject);
                    } catch (JSONException e) {

                    } catch (GoddbException e) {

                    }

                }
                long endWriteStr = System.nanoTime();
                long time = endWriteStr - beginWriteStr;

                textView.setText(" PUT 소요시간 : " + time + "초 걸림.");
            }
        });

        //get test
        god_get_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long beginWriteStr = System.nanoTime();

                for (int i = 0; i < 1000; i++) {

                    String random_path = Double.toString(Math.random() * 1000);
                    String random_condition = Double.toString(Math.random() * 1000);
                    try {

                        JSONArray ret = godDB.get(random_path, random_condition);

                    } catch (GoddbException e) {
                    }

                    long endWriteStr = System.nanoTime();
                    long time = endWriteStr - beginWriteStr;

                    textView.setText(" GET 소요시간 : " + time + "초 걸림.");

                }
            }


        });


    }

    protected void onDestroy(Bundle savedInstanceState) {
        try {
            godDB.close();
        } catch (GoddbException e) {
            e.printStackTrace();
        }
    }
}