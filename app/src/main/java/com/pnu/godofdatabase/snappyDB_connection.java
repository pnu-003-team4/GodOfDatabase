package com.pnu.godofdatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

public class snappyDB_connection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            DB snappydb = DBFactory.open(this); //create or open an existing database using the default name

            snappydb.put("name", "Jack Reacher");
            snappydb.putInt("age", 42);
            snappydb.putBoolean("single", true);
            snappydb.put("books", new String[]{"One Shot", "Tripwire", "61 Hours"});

            String 	 name   =  snappydb.get("name");
            int 	   age    =  snappydb.getInt("age");
            boolean  single =  snappydb.getBoolean("single");
            String[] books  =  snappydb.getArray("books", String.class);// get array of string

            snappydb.close();

        } catch (SnappydbException e) {
        }
    }

}
