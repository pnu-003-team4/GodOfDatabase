package com.pnu.godofdatabase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SQLite_connection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_connection);

        final SQLiteDbManager sqliteDbManager = new SQLiteDbManager(getApplicationContext(), "Food.db", null, 1);

        // DB에 저장 될 속성을 입력받는다
        final EditText etName = findViewById(R.id.et_name);
        final EditText etAge = findViewById(R.id.et_age);

        // 쿼리 결과 입력
        final TextView tvResult = findViewById(R.id.tv_result);

        // Insert
        Button btnInsert = findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // insert into 테이블명 values (값, 값, 값...);
                String name = etName.getText().toString();
                String age = etAge.getText().toString();
                sqliteDbManager.insert("insert into MYLIST values(null, '" + name + "', " + age + ");");

                tvResult.setText(sqliteDbManager.PrintData());
            }
        });

        // Update
        Button btnUpdate = findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // update 테이블명 where 조건 set 값;
                String name = etName.getText().toString();
                String age = etAge.getText().toString();
                sqliteDbManager.update("update FOOD_LIST set price = " + age + " where name = '" + name + "';");

                tvResult.setText(sqliteDbManager.PrintData());
            }
        });

        // Delete
        Button btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // delete from 테이블명 where 조건;
                String name = etName.getText().toString();
                sqliteDbManager.delete("delete from FOOD_LIST where name = '" + name + "';");

                tvResult.setText(sqliteDbManager.PrintData());
            }
        });

        // Select
        Button btnSelect = findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tvResult.setText(sqliteDbManager.PrintData());
            }
        });
    }
}
