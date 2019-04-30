/*
 * Copyright (C) 2013 Nabil HACHICHA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goddb.internal;

import android.text.TextUtils;

import com.esotericsoftware.kryo.Kryo;
import com.goddb.DB;
import com.goddb.GoddbException;
import com.goddb.KeyIterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DBImpl implements DB {
    private static final String LIB_NAME = "goddb-native";
    private static final int LIMIT_MAX = Integer.MAX_VALUE - 8;

    private String dbPath;
    private Kryo kryo;

    static {
        System.loadLibrary(LIB_NAME);
    }

    public DBImpl(String path, Kryo... kryo) throws GoddbException {
        this.dbPath = path;

        if (null != kryo && kryo.length > 0) {
            this.kryo = kryo[0];

        } else {
            this.kryo = new Kryo();
            this.kryo.setAsmEnabled(true);
        }
        __open(dbPath);
    }

    // ***********************
    // *     DB MANAGEMENT
    // ***********************

    @Override
    public void close() {
        __close();
    }

    @Override
    public void destroy() throws GoddbException {
        __destroy(dbPath);
    }

    @Override
    public boolean isOpen() throws GoddbException {
        return __isOpen();
    }


    // ***********************
    // *       INSERT
    // ***********************
    @Override
    public void put(String path, JSONObject object) throws GoddbException {
        checkArgs(path, object); // path, object가 null 인지 확인

        if (!exists(path)) {
            JSONArray newArray = new JSONArray();
            newArray.put(object);
            __put(path, newArray.toString());
        } else {
            try {
                JSONArray oldArray = new JSONArray(__get(path));
                oldArray.put(object);
                __put(path, oldArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void put(String path, JSONArray objects) throws GoddbException {
        checkArgs(path, objects);

        if (!exists(path)) {
            JSONArray newArray = new JSONArray();
            newArray.put(objects);
            __put(path, newArray.toString());
        } else {
            try {
                JSONArray oldArray = new JSONArray(__get(path));
                oldArray.put(objects);
                __put(path, oldArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // ***********************
    // *      DELETE
    // ***********************

    @Override
    public void del(String path, String condition) throws GoddbException {
        /*
         * TODO : path가 존재하는지 확인하고, 존재한다면 wildcard와 condition을 확인하여 해당하는 오브젝트들 제거
         * TODO : wlidcard는 com.goddb.internal.Wildcard.java의 extractWildcard()를 활용한다. condition은 com.goddb.internal.Condition.java의 extractCondtion()을 활용한다.
         */
        boolean chk = false;

        if (exists(path)) {
            try {
                JSONArray oldArray = new JSONArray(__get(path));
                JSONArray conditionArray, retArray = new JSONArray();

                conditionArray = Condition.extractCondition(oldArray, condition);

                ArrayList<String> wildcardArrayList = Wildcard.extractWildcard(path, null);

                if (wildcardArrayList == null) {
                    for (int i = 0; i < oldArray.length(); i++) {
                        for (int j = 0; j < conditionArray.length(); j++) {
                            if (oldArray.getJSONObject(i).toString().equals(conditionArray.getJSONObject(j).toString())) {
                                chk = true;
                            }
                        }
                        if (!chk) {
                            retArray.put(oldArray.getJSONObject(i));
                        }
                        chk = false;
                    }
                    __del(path);
                    __put(path, retArray.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deldir(String path, String condition) {
        /*
         * TODO : path가 존재하는지 확인하고, 존재한다면 wildcard와 condition을 확인하여 해당하는 path들 제거
         * TODO : wlidcard는 com.goddb.internal.Wildcard.java의 extractWildcard()를 활용한다. condition은 com.goddb.internal.Condition.java의 extractCondtion()을 활용한다.
         */

    }

    // ***********************
    // *       RETRIEVE
    // ***********************

    @Override
    public JSONArray get(String path, String condition) throws GoddbException {
        JSONArray retData;

        /*
        JSONArray result;

        ArrayList<String> paths = Wildcard.extractWildcard(path);

        for (String p : paths) {
            if(exists(path)) {
                retData.put(__get(path));
            }
        }
        */

        try {
            if (exists(path)) {
                retData = new JSONArray(__get(path));
            } else {
                return null;
            }
            if (condition.getBytes().length <= 0)
                return retData;
            else
                return Condition.extractCondition(retData, condition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //****************************
    //*      KEYS OPERATIONS
    //****************************
    @Override
    public boolean exists(String key) throws GoddbException {
        checkKey(key);

        return __exists(key);
    }

    @Override
    public String[] findKeys(String prefix) throws GoddbException {
        return findKeys(prefix, 0, LIMIT_MAX);
    }

    @Override
    public String[] findKeys(String prefix, int offset) throws GoddbException {
        return findKeys(prefix, offset, LIMIT_MAX);
    }

    @Override
    public String[] findKeys(String prefix, int offset, int limit) throws GoddbException {
        checkPrefix(prefix);
        checkOffsetLimit(offset, limit);

        return __findKeys(prefix, offset, limit);
    }

    @Override
    public int countKeys(String prefix) throws GoddbException {
        checkPrefix(prefix);

        return __countKeys(prefix);
    }

    @Override
    public String[] findKeysBetween(String startPrefix, String endPrefix)
            throws GoddbException {
        return findKeysBetween(startPrefix, endPrefix, 0, LIMIT_MAX);
    }

    @Override
    public String[] findKeysBetween(String startPrefix, String endPrefix, int offset)
            throws GoddbException {
        return findKeysBetween(startPrefix, endPrefix, offset, LIMIT_MAX);
    }

    @Override
    public String[] findKeysBetween(String startPrefix, String endPrefix, int offset, int limit)
            throws GoddbException {
        checkRange(startPrefix);
        checkOffsetLimit(offset, limit);

        return __findKeysBetween(startPrefix, endPrefix, offset, limit);
    }

    @Override
    public int countKeysBetween(String startPrefix, String endPrefix)
            throws GoddbException {
        checkRange(startPrefix);

        return __countKeysBetween(startPrefix, endPrefix);
    }

    //***********************
    //*      ITERATORS
    //***********************
    @Override
    public KeyIterator allKeysIterator()
            throws GoddbException {
        return new KeyIteratorImpl(this, __findKeysIterator(null, false), null, false);
    }

    @Override
    public KeyIterator allKeysReverseIterator()
            throws GoddbException {
        return new KeyIteratorImpl(this, __findKeysIterator(null, true),  null, true);
    }

    @Override
    public KeyIterator findKeysIterator(String prefix)
            throws GoddbException {
        return new KeyIteratorImpl(this, __findKeysIterator(prefix, false), null, false);
    }

    @Override
    public KeyIterator findKeysReverseIterator(String prefix)
            throws GoddbException {
        return new KeyIteratorImpl(this, __findKeysIterator(prefix, true), null, true);
    }

    @Override
    public KeyIterator findKeysBetweenIterator(String startPrefix, String endPrefix)
            throws GoddbException {
        return new KeyIteratorImpl(this, __findKeysIterator(startPrefix, false), endPrefix, false);
    }

    @Override
    public KeyIterator findKeysBetweenReverseIterator(String startPrefix, String endPrefix)
            throws GoddbException {
        return new KeyIteratorImpl(this, __findKeysIterator(startPrefix, true), endPrefix, true);
    }

    //*********************************
    //*      KRYO SERIALIZATION
    //*********************************
    @Override
    public Kryo getKryoInstance() {
        return this.kryo;
    }

    // ***********************
    // *      UTILS
    // ***********************

    private void checkArgs(String key, Object value) throws GoddbException {
        checkArgNotEmpty (key, "Key must not be empty");

        if (null == value) {
            throw new GoddbException("Value must not be empty");
        }
    }

    private void checkPrefix(String prefix) throws GoddbException {
        checkArgNotEmpty (prefix, "Starting prefix must not be empty");
    }

    private void checkRange(String startPrefix) throws GoddbException {
        checkArgNotEmpty (startPrefix, "Starting prefix must not be empty");
        checkArgNotEmpty (startPrefix, "Ending prefix must not be empty");
    }

    private void checkKey(String key) throws GoddbException {
        checkArgNotEmpty (key, "Key must not be empty");
    }

    private void checkArgNotEmpty(String arg, String errorMsg) throws GoddbException {
        if (TextUtils.isEmpty(arg)) {
            throw new GoddbException(errorMsg);
        }
    }

    private void checkOffsetLimit(int offset, int limit) throws GoddbException {
        if (offset < 0) {
            throw new GoddbException("Offset must not be negative");
        }
        if (limit <= 0) {
            throw new GoddbException("Limit must not be 0 or negative");
        }
    }


    // native code
    private native void __close();

    private native void __open(String dbName) throws GoddbException;

    private native void __destroy(String dbName) throws GoddbException;

    private native boolean __isOpen() throws GoddbException;

    private native void __put(String key, String value) throws GoddbException;

    private native void __del(String key) throws GoddbException;

    private native String __get(String key) throws GoddbException;

    private native boolean __exists(String key) throws GoddbException;

    private native String[] __findKeys(String prefix, int offset, int limit) throws GoddbException;

    private native int __countKeys(String prefix) throws GoddbException;

    private native String[] __findKeysBetween(String startPrefix, String endPrefix, int offset, int limit) throws GoddbException;

    private native int __countKeysBetween(String startPrefix, String endPrefix) throws GoddbException;

    native long __findKeysIterator(String prefix, boolean reverse) throws GoddbException;

    native String[] __iteratorNextArray(long ptr, String endPrefix, boolean reverse, int max) throws GoddbException;

    native boolean __iteratorIsValid(long ptr, String endPrefix, boolean reverse);

    native void __iteratorClose(long ptr);
}
