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

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText inputPath;
    EditText inputData;
    EditText inputCondition;
    Button inputBtn;
    Button outputBtn;
    Button delBtn;
    Button modBtn;
    TextView resultText;
    DB godDB; //create or open an existing database using the default name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPath = findViewById(R.id.inputPath);
        inputData = findViewById(R.id.inputData);
        inputCondition = findViewById(R.id.inputCondition);
        inputBtn = findViewById(R.id.inputBtn);
        outputBtn = findViewById(R.id.outputBtn);
        delBtn = findViewById(R.id.delBtn);
        modBtn = findViewById(R.id.modBtn);
        resultText = findViewById(R.id.result);

        try {
            godDB = DBFactory.open(this); //create or open an existing database using the default name
        } catch (GoddbException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        inputBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject obj = new JSONObject();

                try {
                    String[] section = inputData.getText().toString().split(",");
                    for (int k = 0; k < section.length; k++) {
                        String[] data = section[k].split("=");
                        obj.put(data[0], data[1]);
                    }
                    putTest(inputPath.getText().toString(), obj);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });

        outputBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONArray temp = getTest(inputPath.getText().toString(), inputCondition.getText().toString());

                if (temp != null) {
                    resultText.setText(temp.toString());
                } else {
                    Log.d("get", "null.");
                }
            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (inputCondition.getText().toString().isEmpty()) {
                    delpathTest(inputPath.getText().toString());
                } else {
                    delTest(inputPath.getText().toString(), inputCondition.getText().toString());
                }
            }
        });

        modBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                modTest(inputPath.getText().toString(), inputCondition.getText().toString(), inputData.getText().toString());
            }
        });
    }

    protected void onStop(Bundle savedInstanceState) {
        try {
            godDB.close();
        } catch (GoddbException | IOException e) {
            e.printStackTrace();
        }
    }

    void putTest(String path, JSONObject obj) {
        try {
            godDB.put(path, obj);
            resultText.setText("put");
        } catch (GoddbException | IOException e) {
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
            JSONArray retArray = godDB.del(path, condition);
            resultText.setText("delete: " + retArray.toString());
        } catch (GoddbException | IOException e) {
            e.printStackTrace();
        }
    }

    void delpathTest(String path) {
        try {
            JSONArray retArray = godDB.deldir(path);
            resultText.setText("delete: " + retArray.toString());
        } catch (GoddbException | IOException e) {
            e.printStackTrace();
        }
    }

    void modTest(String path, String condition, String data) {
        try {
            godDB.update(path, condition, data);
            resultText.setText("Update Done.");
        } catch (GoddbException e) {
            e.printStackTrace();
        }
    }
}
