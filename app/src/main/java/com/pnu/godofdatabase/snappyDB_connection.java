package com.pnu.godofdatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class snappyDB_connection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snappydb_connection);

        try {
            final DB snappydb = DBFactory.open(this);

            final EditText edit_put = (EditText) findViewById(R.id.edit_put);
            final EditText edit_select = (EditText) findViewById(R.id.edit_select);
            final EditText edit_del = (EditText) findViewById(R.id.edit_del);

            final TextView textView = (TextView) findViewById(R.id.output);

            Button btn_put = (Button) findViewById(R.id.btn_put);
            Button btn_select = (Button) findViewById(R.id.btn_select);
            Button btn_del = (Button) findViewById(R.id.btn_del);

            btn_put.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String input = edit_put.getText().toString();
                    String key = null, value = null;
                    int idx = input.indexOf(",");
                    if (idx != -1) {
                        key = input.substring(0, idx);
                        value = input.substring(idx + 1);
                    } else {
                        Toast.makeText(getApplicationContext(), "Usage: 'key', 'value' 입력", Toast.LENGTH_LONG).show();
                        edit_put.setText(null);
                        textView.setText(null);
                    }

                    try {
                        snappydb.put(key, value);
                        Toast.makeText(getApplicationContext(), "입력되었습니다", Toast.LENGTH_LONG).show();
                        edit_put.setText(null);
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "입력되지못했습니다", Toast.LENGTH_LONG).show();
                    }
                }
            });

            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String key = edit_del.getText().toString();
                    try {
                        snappydb.del(key);
                        Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
                        edit_del.setText(null);
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "삭제되지못했습니다", Toast.LENGTH_LONG).show();
                    }

                }
            });

            btn_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String output = edit_select.getText().toString();
                    String value;
                    edit_select.setText(null);
                    try {
                        value = snappydb.get(output);
                        textView.setText(value);
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "value 값이 없습니다", Toast.LENGTH_LONG).show();
                        edit_select.setText(null);
                        textView.setText(null);
                    }
                }
            });

        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

}
//hyewon test haha