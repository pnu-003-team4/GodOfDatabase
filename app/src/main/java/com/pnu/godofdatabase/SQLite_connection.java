package com.pnu.godofdatabase;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SQLite_connection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_connection);

        final SQLiteDbManager sqliteDbManager = new SQLiteDbManager(getApplicationContext(), "Food.db", null, 1);

        // DB에 저장 될 속성을 입력받는다
//        final EditText etName = findViewById(R.id.et_name);
//        final EditText etAge = findViewById(R.id.et_age);

        final EditText edit_put = (EditText)findViewById(R.id.edit_put);
        final EditText edit_select = (EditText)findViewById(R.id.edit_select);
        final EditText edit_del = (EditText)findViewById(R.id.edit_del);
        final EditText edit_update = (EditText)findViewById(R.id.edit_update);

        final TextView textView = (TextView)findViewById(R.id.output);

        Button btn_put = (Button) findViewById(R.id.btn_put);
        Button btn_select = (Button) findViewById(R.id.btn_select);
        Button btn_del = (Button)findViewById(R.id.btn_del);
        Button btn_update = (Button)findViewById(R.id.btn_update);

        // Insert
        btn_put.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // insert into 테이블명 values (값, 값, 값...);
                String query = edit_put.getText().toString();

//                sqliteDbManager.insert("insert into MYLIST values(null, '" + name + "', " + age + ");");
                sqliteDbManager.insert(query);
                textView.setText(sqliteDbManager.PrintData());
                Toast.makeText(getApplicationContext(), "입력되었습니다", Toast.LENGTH_LONG).show();
            }
        });

        // Update
        btn_update.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // update 테이블명 where 조건 set 값;
                String query = edit_update.getText().toString();

                sqliteDbManager.update(query);
                textView.setText(sqliteDbManager.PrintData());
                Toast.makeText(getApplicationContext(), "수정되었습니다", Toast.LENGTH_LONG).show();
            }
        });

        // Delete
        btn_del.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // delete from 테이블명 where 조건;
                String query = edit_del.getText().toString();

                sqliteDbManager.delete(query);
                textView.setText(sqliteDbManager.PrintData());
                Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
            }
        });

//        // Select
        btn_select.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String query = edit_select.getText().toString();
                if(query.isEmpty()){
                    Toast.makeText(getApplicationContext(), "if문", Toast.LENGTH_LONG).show();
                    textView.setText(sqliteDbManager.PrintData());
                }
                else{
                    Toast.makeText(getApplicationContext(), "else 문", Toast.LENGTH_LONG).show();
                    textView.setText(sqliteDbManager.ConditionPrintData(query));
                }
//                textView.setText(sqliteDbManager.PrintData());
                Toast.makeText(getApplicationContext(), "검색되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
