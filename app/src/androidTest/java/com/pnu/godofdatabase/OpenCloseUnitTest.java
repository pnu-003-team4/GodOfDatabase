package com.pnu.godofdatabase;

import com.goddb.DB;
import com.goddb.DBFactory;
import com.goddb.GoddbException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class OpenCloseUnitTest {
    DB db;

    @Before
    public void setUp() throws GoddbException {
        db = DBFactory.open("test", "test");
    }

    //TODO: Make your test code
    @Test
    public void putTest() throws JSONException, GoddbException {
        JSONObject jobject = new JSONObject();
        jobject.put("key", "value");

        db.put("abc", jobject);
        assertEquals(jobject, db.get("abc", null).get(0));
    }

    @After
    public void tearDown() throws GoddbException {
        //db.close();
    }
}