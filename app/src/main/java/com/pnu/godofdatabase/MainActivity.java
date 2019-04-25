package com.pnu.godofdatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.goddb.DB;
import com.goddb.DBFactory;
import com.goddb.GoddbException;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snappydb_connection);
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

            godDB.del("Korea/Busan/University/PNU", "name==ParkJeongHwan");

            godDB.close();
        } catch (GoddbException e) {
            e.printStackTrace();
        }
    }
}
