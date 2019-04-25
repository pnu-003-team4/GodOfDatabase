package com.pnu.godofdatabase;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SQLite_connection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_connection);

        final EditText edit_result = findViewById(R.id.editText1);
        Button btn_Save = findViewById(R.id.button1);
        Button btn_Load = findViewById(R.id.button2);
        final TextView tv = findViewById(R.id.textView1);

        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override    // 입력한 데이터를 파일에 추가로 저장하기
            public void onClick(View v) {
                String data = edit_result.getText().toString();

                try {
                    FileOutputStream fos = openFileOutput
                            ("file.txt", // 파일명 지정
                                    Context.MODE_APPEND);// 저장모드
                    PrintWriter out = new PrintWriter(fos);
                    out.println(data);
                    out.close();

                    tv.setText("파일 저장 완료");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn_Load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 파일의 내용을 읽어서 TextView 에 보여주기
                try {
                    // 파일에서 읽은 데이터를 저장하기 위해서 만든 변수
                    StringBuffer data = new StringBuffer();
                    FileInputStream fis = openFileInput("myfile.txt");//파일명
                    BufferedReader buffer = new BufferedReader
                            (new InputStreamReader(fis));
                    String str = buffer.readLine(); // 파일에서 한줄을 읽어옴
                    while (str != null) {
                        data.append(str + "\n");
                        str = buffer.readLine();
                    }
                    tv.setText(data);
                    buffer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    } // end of onCreate
} // end of class
