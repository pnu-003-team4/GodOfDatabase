package com.pnu.godofdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Main_app extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        Button btn_GOD = findViewById(R.id.btn_GOD);
        Button btn_sqlite = findViewById(R.id.btn_sqlite);
        Button btn_snappy = findViewById(R.id.btn_snappy);
        Button btn_per = findViewById(R.id.btn_cal);


        btn_GOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_app.this, GOD_connection.class);
                startActivity(intent);
            }
        });

        btn_snappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_app.this, snappyDB_connection.class);
                startActivity(intent);
            }
        });

        btn_sqlite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_app.this, SQLite_connection.class);
                startActivity(intent);
            }
        });

        btn_per.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main_app.this, performance.class);
                startActivity(intent);
            }
        });
    }
}