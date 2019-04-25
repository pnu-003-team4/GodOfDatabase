package com.pnu.godofdatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.goddb.DB;
import com.goddb.DBFactory;
import com.goddb.GoddbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GOD_connection extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_god_connection);

        try {
            DB godDB = DBFactory.open("DB"); //create or open an existing database using the default name

            final TextView textView = (TextView)findViewById(R.id.textView);
            JSONObject student = new JSONObject();

            try {
                student.put("name", "JSON");
                student.put("age", "23");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            godDB.put("/Korea/Busan/University/PNU", student);
            godDB.get("/Korea/Busan/*/PNU", null);
            godDB.del("Korea/Busan/University/PNU", "name==ParkJeongHwan");

            godDB.close();
        } catch (GoddbException e) {
            e.printStackTrace();
        }
    }
}
