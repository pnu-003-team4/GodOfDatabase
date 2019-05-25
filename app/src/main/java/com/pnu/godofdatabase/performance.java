package com.pnu.godofdatabase;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappyDB;
import com.snappydb.SnappydbException;

import java.util.HashMap;

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
                    .append(Math.random()).substring(0, 100);

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

        DB db = new SnappyDB.Builder(this)
                .name("reference_bench_string")
                .build();
        long beginWriteStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            db.put(keys[i], values[i]);
        }
        long endWriteStr = System.nanoTime();

        long beginReadStr = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            db.get(keys[i]);
        }
        long endReadStr = System.nanoTime();
        db.destroy();

        writestr = (double)(endWriteStr - beginWriteStr)/1000000;
        readstr = (double)(endReadStr - beginReadStr)/1000000;


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

    public void testSqlite(){
        SQLiteDbManager sqliteDbManager = new SQLiteDbManager(getApplicationContext(), "Food.db", null, 1);
        long beginWriteStr = System.nanoTime();
        for(int i=0; i<1000; i ++){
            int num = (int)Math.random() * 1000;

            sqliteDbManager.insert("insert into MYLIST values(null, 'test', " + num + ");");
        }
        long endWriteStr = System.nanoTime();

        long beginReadStr = System.nanoTime();
        sqliteDbManager.PrintData();
        long endReadStr = System.nanoTime();
        sqliteDbManager.close();

        writestr = (double)(endWriteStr - beginWriteStr)/1000000;
        readstr = (double)(endReadStr - beginReadStr)/1000000;

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

        btn_sqlite_test.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                testSqlite();
            }
        });
    }
}


