package com.pnu.godofdatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.goddb.GodDB;
import com.goddb.GoddbException;
import com.snappydb.DB;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class performance extends AppCompatActivity {

    Button btn_snappy_test;
    Button btn_god_test;
    Button btn_sqlite_test;
    Button btn_chart;

    float writestr_snappy, readstr_snappy;
    float writestr_god, readstr_god;
    float writestr_sqlite, readstr_sqlite;

    DB godDB;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testReferenceSetup() throws SnappydbException {
        String keys[] = new String[1000];
        String values[] = new String[1000];

        String key, value;
        for (int i = 0; i < 1000; i++) {
            //Generate key
            key = new StringBuilder()
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random()).substring(0, 16);

            //Generate value
            value = new StringBuilder()
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random()).substring(0, 100);

            keys[i] = key;
            values[i] = value;
        }

        writestr_snappy = (float) (endWriteStr - beginWriteStr) / 1000000;
        readstr_snappy = (float) (endReadStr - beginReadStr) / 1000000;

        DB db = new SnappyDB.Builder(this)
                .name("reference_bench_string")
                .build();
        long beginWriteStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            sqliteDbManager.insert("insert into MYLIST values(null, 'test', " + values[i] + ");");
        }
        long endWriteStr = System.nanoTime();

        long beginReadStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            db.get(keys[i]);
        }
        long endReadStr = System.nanoTime();
        db.destroy();

        writestr = (double) (endWriteStr - beginWriteStr) / 1000000;
        readstr = (double) (endReadStr - beginReadStr) / 1000000;


        AlertDialog.Builder alert = new AlertDialog.Builder(performance.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("1000 write of String in " + String.valueOf(writestr) + "ms\n"
                + "1000 read of String in " + String.valueOf(readstr) + "ms\n");
        alert.show();
    }

    public void testSqlite() {
        SQLiteDbManager sqliteDbManager = new SQLiteDbManager(getApplicationContext(), "Food.db", null, 1);
        long beginWriteStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            int num = (int) Math.random() * 1000;

            sqliteDbManager.insert("insert into MYLIST values(null, 'test', " + num + ");");
        }
        long endWriteStr = System.nanoTime();

        long beginReadStr = System.nanoTime();
        sqliteDbManager.PrintData();
        long endReadStr = System.nanoTime();
        sqliteDbManager.close();

        writestr = (double) (endWriteStr - beginWriteStr) / 1000000;
        readstr = (double) (endReadStr - beginReadStr) / 1000000;

        AlertDialog.Builder alert = new AlertDialog.Builder(performance.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("1000 write of String in " + String.valueOf(writestr) + "ms\n"
                + "1000 read of String in " + String.valueOf(readstr) + "ms\n");
        alert.show();
    }
    public void testGOD() throws GoddbException {
        com.goddb.DB godDB = null;
        try {
            godDB = new GodDB.Builder(this)
                    .name("godDB1")
                    .build();
        } catch (GoddbException e) {

        }

        long beginWriteStr = System.nanoTime();
        String static_path = Double.toString(1000);
        for (int i = 0; i < 1000; i++) {
            JSONObject jsonObject = new JSONObject();

            String random_obj = Double.toString(Math.random() * 1000);
            try {
                jsonObject.put("val", random_obj);
                godDB.put(static_path, jsonObject);
            } catch (JSONException e) {

            } catch (GoddbException e) {

            }

        }
        long endWriteStr = System.nanoTime();

        long beginReadStr = System.nanoTime();

        try {

            JSONArray ret = godDB.get(static_path, "*");

        } catch (GoddbException e) {
        }

        long endReadStr = System.nanoTime();
        godDB.destroy();

        writestr_god = (float) (endWriteStr - beginWriteStr) / 1000000;
        readstr_god = (float) (endReadStr - beginReadStr) / 1000000;

        writestr = (double) (endWriteStr - beginWriteStr) / 1000000;
        readstr = (double) (endReadStr - beginReadStr) / 1000000;
    }

    public void alertDialog(float writestr, float readstr){
        AlertDialog.Builder alert = new AlertDialog.Builder(performance.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("1000 write of String in " + String.valueOf(writestr) + "ms\n"
                + "1000 read of String in " + String.valueOf(readstr) + "ms\n");
        alert.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        btn_snappy_test = findViewById(R.id.btn_snappy_test);
        btn_god_test = findViewById(R.id.btn_god_test);
        btn_sqlite_test = findViewById(R.id.btn_sqlite_test);
        btn_chart = findViewById(R.id.btn_chart);

        try {
            godDB = new GodDB.Builder(this)
                    .name("godDB1")
                    .build();
        } catch (GoddbException | IOException | ClassNotFoundException e) {

        }

        btn_snappy_test.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                try {
                    testReferenceSetup();
                } catch (SnappydbException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_sqlite_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                testSqlite();
            }
        });

        btn_god_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    testGOD();
                } catch (GoddbException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


