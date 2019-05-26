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

public class GOD_connection extends AppCompatActivity {
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
        setContentView(R.layout.activity_god_connection);

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
        } catch (GoddbException e) {
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
                String sort = "age=<<";
                JSONArray temp = getTest(inputPath.getText().toString(), inputCondition.getText().toString(), sort);

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

    JSONArray getTest(String path, String condition, String sort) {
        String[] section = sort.split("=");
        // section[0] = age, section[1] = <<
        try {
            JSONArray sortArr = new JSONArray();
            JSONArray retArr = godDB.get(path, condition);


            if (section[1] == "<<") {
                int i = 0, j = 0, border = 0, index = 0;
                for (; i < retArr.length(); i++) {
                    int tmp = retArr.getJSONObject(i).getInt(section[0]);
                    for (; j < retArr.length(); j++) {
                        int comp = retArr.getJSONObject(j).getInt(section[0]);
                        if (comp > border && comp < tmp) {
                            comp = tmp;
                            index = j;
                        }
                        border = comp;
                    }
                    sortArr.put(retArr.getJSONObject(index));
                }
            } else if (section[1] == ">>") {
                int i = 0, j = 0, border = 1000000, index = 0;
                for (; i < retArr.length(); i++) {
                    int tmp = retArr.getJSONObject(i).getInt(section[0]);
                    for (; j < retArr.length(); j++) {
                        int comp = retArr.getJSONObject(j).getInt(section[0]);
                        if (comp < border && comp > tmp) {
                            comp = tmp;
                            index = j;
                        }
                        border = comp;
                    }
                    sortArr.put(retArr.getJSONObject(index));
                }
            } else {
                //error
            }

            return sortArr;

        } catch (GoddbException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    void delTest(String path, String condition) {
        try {
            JSONArray retArray = godDB.del(path, condition);
            resultText.setText("delete: " + retArray.toString());
        } catch (GoddbException e) {
            e.printStackTrace();
        }
    }

    void delpathTest(String path) {
        try {
            JSONArray retArray = godDB.deldir(path);
            resultText.setText("delete: " + retArray.toString());
        } catch (GoddbException e) {
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
