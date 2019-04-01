package com.pnu.godofdatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            DB snappydb = DBFactory.open("DB"); //create or open an existing database using the default name

            JSONObject student = new JSONObject();

            try {
                student.put("name", "JSON");
                student.put("age", "23");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            snappydb.put("/Korea/Busan/University/PNU", student);
            snappydb.get("/Korea/Busan/*/PNU", null);

            snappydb.close();

        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
