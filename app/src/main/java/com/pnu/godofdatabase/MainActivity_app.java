package com.pnu.godofdatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_GOD = (Button) findViewById(R.id.btn_GOD);
        Button btn_sqlite = (Button) findViewById(R.id.btn_sqlite);
        Button btn_snappy = (Button)findViewById(R.id.btn_snappy);
        Button btn_cal = (Button)findViewById(R.id.btn_cal);


        btn_GOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GOD_connection.class);
                startActivity(intent);
            }
        });

        btn_snappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, snappyDB_connection.class);
                startActivity(intent);
            }
        });

        btn_sqlite.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, SQLite_connection.class);
                startActivity(intent);
            }
        });
    }
}
