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
        setContentView(R.layout.activity_main);

        try {
            final DB snappydb = DBFactory.open(this);

            final EditText edit_put = (EditText)findViewById(R.id.edit_put);
            final EditText edit_select = (EditText)findViewById(R.id.edit_select);
            final EditText edit_del = (EditText)findViewById(R.id.edit_del);
            final EditText edit_update = (EditText)findViewById(R.id.edit_update);

            final TextView textView = (TextView)findViewById(R.id.output);

            Button btn_put = (Button) findViewById(R.id.btn_put);
            Button btn_select = (Button) findViewById(R.id.btn_select);
            Button btn_del = (Button)findViewById(R.id.btn_del);
            Button btn_update = (Button)findViewById(R.id.btn_update);

            btn_put.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String input = edit_put.getText().toString();
                    String key = null;
                    String[] array = input.split(",");
                    String[] value = new String[array.length-1];
                    if(array.length >= 2) {
                        key = array[0];
                        value(array,value);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Usage: 'key', 'value' 입력",Toast.LENGTH_LONG).show();
                        edit_put.setText(null);
                        textView.setText(null);
                    }

                    try {
                        if(snappydb.exists(key)) {
                            Toast.makeText(getApplicationContext(), "이미 입력된 key입니다.", Toast.LENGTH_LONG).show();
                            edit_put.setText(null);
                        }
                        else {
                            snappydb.put(key, value);
                            Toast.makeText(getApplicationContext(), "입력되었습니다", Toast.LENGTH_LONG).show();
                            edit_put.setText(null);
                        }
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"입력되지못했습니다",Toast.LENGTH_LONG).show();
                        edit_put.setText(null);
                    }
                }
            });

            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String key = edit_del.getText().toString();
                    try {
                        if(snappydb.exists(key)) {
                            snappydb.del(key);
                            Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
                            edit_del.setText(null);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "존재하지 않는 key입니다", Toast.LENGTH_LONG).show();
                            edit_del.setText(null);
                        }
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"삭제되지못했습니다",Toast.LENGTH_LONG).show();
                        edit_del.setText(null);
                    }

                }
            });

            btn_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String output = edit_select.getText().toString();
                    String[] value = null;
                    edit_select.setText(null);
                    textView.setText(null);
                    try {
                        value = snappydb.getArray(output,String.class);
                        for(int i=0;i<value.length;i++)
                            textView.append(value[i]+"\n");
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"value값이 없습니다",Toast.LENGTH_LONG).show();
                        edit_select.setText(null);
                        textView.setText(null);
                    }
                }
            });

            btn_update.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    String input = edit_update.getText().toString();
                    String key = null;
                    String[] array = input.split(",");
                    String[] value = new String[array.length-1];
                    if(array.length >= 2) {
                        key = array[0];
                        value(array,value);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Usage: 'key', 'value' 입력",Toast.LENGTH_LONG).show();
                        edit_update.setText(null);
                        textView.setText(null);
                    }

                    try {
                        if(snappydb.exists(key)) {
                            snappydb.put(key, value);
                            Toast.makeText(getApplicationContext(), key + "의 값이 수정되었습니다", Toast.LENGTH_LONG).show();
                            edit_update.setText(null);
                            textView.setText(null);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), key + "가 존재하지 않습니다", Toast.LENGTH_LONG).show();
                            edit_update.setText(null);
                        }
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"수정되지 못하였습니다",Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    public void value(String[] array, String[] value){
        for(int i = 0 ; i < array.length-1 ; i++) {
            if(i==0) {
                String str = array[i+1].substring(1);
                value[i] = str;
            }
            else if(i == array.length-2){
                int length = array[i+1].length();
                String str = array[i+1].substring(0,length-1);
                value[i] = str;
            }
            else
                value[i] = array[i+1];
        }
    }
}
