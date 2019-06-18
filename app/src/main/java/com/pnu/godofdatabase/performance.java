package com.pnu.godofdatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.goddb.GodDB;
import com.goddb.GoddbException;
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
    TextView result;

    float writestr_snappy, readstr_snappy;
    float writestr_god, readstr_god;
    float writestr_sqlite, readstr_sqlite;
    String staticPath = "/test";

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testReferenceSetup() throws SnappydbException, JSONException {
        String[] keys = new String[5000];
        String[] values = new String[5000];

        String key, value;

        String temp="";
        for(int j=5; j<10000; j++)
            temp+='a';

        for (int i = 0; i < 5000; i++) {
            //Generate key
            key = new StringBuilder()
                    .append(temp)
                    .append(Math.random()).substring(0, 10000);

            //Generate value
            value = new StringBuilder()
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random()).substring(0, 10);

            keys[i] = key;
            values[i] = value;
        }
        JSONArray jsona = new JSONArray();

        for(int i=0;i<5000;i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("val", values[i]);
            jsona.put(i, jsonObject);
        }

        com.snappydb.DB db = new SnappyDB.Builder(this)
                .name("reference_bench_string")
                .build();

        long beginWriteStr = System.nanoTime();

        //db.put(staticPath, jsona);
        for(int i=0; i<5000; i++) {
            db.put(keys[i], (JSONObject)jsona.get(i));
        }

        long endWriteStr = System.nanoTime();

        long beginReadStr = System.nanoTime();

        //db.get(staticPath);
        for(int i=0; i<5000; i++) {
            db.get(keys[i]);
        }

        long endReadStr = System.nanoTime();
        db.destroy();

        writestr_snappy = (endWriteStr - beginWriteStr) / 100000;
        readstr_snappy = (endReadStr - beginReadStr) / 100000;


        AlertDialog.Builder alert = new AlertDialog.Builder(performance.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("5000 write of String in " + writestr_snappy + "ms\n"
                + "5000 read of String in " + readstr_snappy + "ms\n");
        alert.show();
    }

    public void testSqlite() {
        SQLiteDbManager sqliteDbManager = new SQLiteDbManager(getApplicationContext(), "Food.db", null, 1);
        long beginWriteStr = System.nanoTime();
        for (int i = 0; i < 5000; i++) {
            int num = (int) Math.random()*5000;

            sqliteDbManager.insert("insert into MYLIST values(null, 'test', " + num + ");");
        }
        long endWriteStr = System.nanoTime();

        long beginReadStr = System.nanoTime();
        sqliteDbManager.PrintData();
        long endReadStr = System.nanoTime();
        sqliteDbManager.close();

        writestr_sqlite = (endWriteStr - beginWriteStr) / 100000;
        readstr_sqlite = (endReadStr - beginReadStr) / 100000;

        AlertDialog.Builder alert = new AlertDialog.Builder(performance.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("5000 write of String in " + writestr_sqlite + "ms\n"
                + "5000 read of String in " + readstr_sqlite + "ms\n");
        alert.show();
    }
    public void testGOD() throws GoddbException, JSONException, IOException {
        com.goddb.DB godDB = null;
        try {
            godDB = new GodDB.Builder(this)
                    .name("godDB1")
                    .build();
        } catch (GoddbException | IOException | ClassNotFoundException e) {

        }
        String[] keys = new String[5000];
        String[] values = new String[5000];

        String key, value;

        String temp="";
        for(int j=5; j<10000; j++)
            temp+='a';

        for (int i = 0; i < 5000; i++) {
            //Generate key
            key = new StringBuilder()
                    .append(temp)
                    .append(Math.random()).substring(0, 10000);

            //Generate value
            value = new StringBuilder()
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random())
                    .append(Math.random()).substring(0, 10);

            keys[i] = key;
            values[i] = value;
        }
        JSONArray jsona = new JSONArray();

        for(int i=0;i<5000;i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("val", values[i]);
            jsona.put(i, jsonObject);
        }

        JSONObject jsonOb = new JSONObject();
        jsonOb.put("value", jsona.toString());

        long beginWriteStr = System.nanoTime();

        //godDB.put(staticPath, jsonOb);

        for(int i=0; i<5000; i++) {
            godDB.put(keys[i], (JSONObject) jsona.get(i));
        }

        long endWriteStr = System.nanoTime();

        long beginReadStr = System.nanoTime();

        //godDB.get(staticPath, null);

        for(int i=0; i<5000; i++) {
            godDB.get(keys[i], null);
        }

        long endReadStr = System.nanoTime();

        godDB.destroy();

        writestr_god = (endWriteStr - beginWriteStr) / 100000;
        readstr_god = (endReadStr - beginReadStr) / 100000;

        AlertDialog.Builder alert = new AlertDialog.Builder(performance.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("5000 write of String in " + writestr_god + "ms\n"
                + "5000 read of String in " + readstr_god + "ms\n");
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
        result = findViewById(R.id.result);

        btn_snappy_test.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(View v) {
                try {
                    testReferenceSetup();
                } catch (SnappydbException | JSONException e) {
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
                } catch (GoddbException | JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btn_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(performance.this, Chart.class);
                intent.putExtra("write_snappy", writestr_snappy);
                intent.putExtra("read_snappy", readstr_snappy);
                intent.putExtra("write_sqlite", writestr_sqlite);
                intent.putExtra("read_sqlite", readstr_sqlite);
                intent.putExtra("write_god", writestr_god);
                intent.putExtra("read_god", readstr_god);
                startActivity(intent);
            }
        });
    }
}


