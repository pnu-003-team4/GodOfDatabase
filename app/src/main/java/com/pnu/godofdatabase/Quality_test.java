package com.pnu.godofdatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Quality_test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality_test);

        final SQLiteDbManager sqliteDbManager = new SQLiteDbManager(getApplicationContext(), "Food.db", null, 1);

        Button sqlite_put_test = findViewById(R.id.sqlite_put_test);
        Button sqlite_get_test = findViewById(R.id.sqlite_get_test);
        final TextView textView = findViewById(R.id.output);


        sqlite_put_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long beginWriteStr = System.nanoTime();
                for (int i = 0; i < 1000; i++) {
                    int num = (int) Math.random() * 1000;

                    sqliteDbManager.insert("insert into MYLIST values(null, 'test', " + num + ");");
//                    textView.setText(sqliteDbManager.PrintData());

                }
                long endWriteStr = System.nanoTime();
                long time = endWriteStr - beginWriteStr;
                Toast.makeText(getApplicationContext(), "소요시간 : " + time, Toast.LENGTH_LONG).show();
            }
        });

        sqlite_get_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long beginWriteStr = System.nanoTime();

//                sqliteDbManager.select("select * from MYLIST");
                textView.setText(sqliteDbManager.PrintData());
                long endWriteStr = System.nanoTime();
                long time = endWriteStr - beginWriteStr;
//                textView.setText((int) time);
                Toast.makeText(getApplicationContext(), "소요시간 : " + time, Toast.LENGTH_LONG).show();
            }
        });
    }
}
