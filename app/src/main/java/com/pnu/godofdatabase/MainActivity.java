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
            DB godDB = DBFactory.open("DB"); //create or open an existing database using the default name

            JSONObject student = new JSONObject();

            try {
                student.put("name", "JSON");
                student.put("age", "23");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            godDB.put("/Korea/Busan/University/PNU", student);
            godDB.get("/Korea/Busan/*/PNU", null);

            godDB.close();

        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }
}
