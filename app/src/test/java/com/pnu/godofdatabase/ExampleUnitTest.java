package com.pnu.godofdatabase;

import com.goddb.DB;
import com.goddb.GoddbException;
import com.goddb.internal.DBImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    DB db;

    @Before
    public void setUp() {
        db = Mockito.mock(DBImpl.class);
        assertTrue(db != null);
    }

    //TODO: Make your test code
    @Test
    public void putTest() throws JSONException, GoddbException {
        JSONObject jobject = new JSONObject();
        jobject.put("key", "value");

        db.put("abc", jobject);

        Mockito.when(db.get("/abc", null).get(0).toString()).thenReturn(jobject.toString());
    }
}