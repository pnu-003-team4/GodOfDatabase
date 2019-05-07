package com.pnu.godofdatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.goddb.DB;
import com.goddb.DBFactory;
import com.goddb.GoddbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText inputPath;
    EditText inputName;
    EditText inputAge;
    Button inputBtn;
    Button outputBtn;
    Button delBtn;
    TextView resultText;
    DB godDB; //create or open an existing database using the default name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPath = findViewById(R.id.inputPath);
        inputName = findViewById(R.id.inputName);
        inputAge = findViewById(R.id.inputAge);
        inputBtn = findViewById(R.id.inputBtn);
        outputBtn = findViewById(R.id.outputBtn);
        delBtn = findViewById(R.id.delBtn);
        resultText = findViewById(R.id.result);

        try {
            godDB = DBFactory.open(this, "test"); //create or open an existing database using the default name
        } catch (GoddbException e) {
            e.printStackTrace();
        }

        inputBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject student = new JSONObject();

                try {
                    student.put("name", inputName.getText());
                    student.put("age", inputAge.getText());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                putTest(String.valueOf(inputPath.getText()), student);
            }
        });

        outputBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONArray temp = getTest(inputPath.getText().toString(), inputAge.getText().toString());

                if (temp != null) {
                    resultText.setText(temp.toString());
                } else {
                    Log.d("get", "null.");
                }
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delTest(inputPath.getText().toString(), inputAge.getText().toString());
            }
        });
    }

    protected void onStop(Bundle savedInstanceState) {
        try {
            godDB.close();
        } catch (GoddbException e) {
            e.printStackTrace();
        }
    }

    void putTest(String path, JSONObject obj) {
        try {
            godDB.put(path, obj);
            resultText.setText("put");
        } catch (GoddbException e) {
            e.printStackTrace();
        }
    }

    JSONArray getTest(String path, String condition) {
        try {
            JSONArray ret = godDB.get(path, condition);

            return ret;
        } catch (GoddbException e) {
            e.printStackTrace();
            return null;
        }
    }

    void delTest(String path, String condition) {
        try {
            godDB.del(path, condition);
            resultText.setText("delete");
        } catch (GoddbException e) {
            e.printStackTrace();
        }
    }
}
