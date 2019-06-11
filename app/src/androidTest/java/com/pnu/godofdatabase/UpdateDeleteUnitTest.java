package com.pnu.godofdatabase;

import com.goddb.DB;
import com.goddb.DBFactory;
import com.goddb.GoddbException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UpdateDeleteUnitTest {
    DB db;

    @Before
    public void setUp() throws GoddbException {
        db = DBFactory.open("test");
    }

    //TODO: Make your test code
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @After
    public void tearDown() throws GoddbException {
        db.close();
    }
}