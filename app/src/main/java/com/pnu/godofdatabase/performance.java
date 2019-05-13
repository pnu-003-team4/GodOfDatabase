package com.pnu.godofdatabase;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;
//import com.snappydb.DBFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static java.security.AccessController.getContext;


public class performance extends AppCompatActivity {

    Button btn_snappy_test;
    Button btn_god_test;
    Button btn_sqlite_test;
    TextView result;
    double writestr, readstr;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void testReferenceSetup() throws SnappydbException{
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
                    .append(Math.random()).substring(0, 1000);

            keys[i] = key;
            values[i] = value;
        }

        HashMap<String, Boolean> keyMap = new HashMap<>((int) Math.ceil(1000 / 0.75));

        for (String k : keys) {
            if (null == keyMap.get(k)) {
                keyMap.put(k, true);
            } else {
                Log.d("fail: ", "duplicate key");
                break;
            }
        }

        // Now we are ready to insert into SnappyDB [String]
        DB db = new SnappyDB.Builder(this)
                .name("reference_bench_string")
                .build();

        long beginWriteStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            db.put(keys[i], values[i]);
        }
        long endWriteStr = System.nanoTime();

        db.close();

        db = new SnappyDB.Builder(this)
                .name("reference_bench_string")
                .build();

        long beginReadStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            db.get(keys[i]);
        }
        long endReadStr = System.nanoTime();

        writestr = (double)(endWriteStr - beginWriteStr)/1000000;
        readstr = (double)(endReadStr - beginReadStr)/1000000;
        db.close();

        AlertDialog.Builder alert = new AlertDialog.Builder(performance.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("1000 write of String in " + String.valueOf(writestr)+"ms\n"
                +"1000 read of String in " + String.valueOf(readstr)+"ms\n");
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);

        btn_snappy_test = findViewById(R.id.btn_snappy_test);
        btn_god_test = findViewById(R.id.btn_god_test);
        btn_sqlite_test = findViewById(R.id.btn_sqlite_test);
        result = findViewById(R.id.result);

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
    }
}


