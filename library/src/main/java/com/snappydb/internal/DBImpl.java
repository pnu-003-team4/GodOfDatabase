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

package com.snappydb.internal;

import android.text.TextUtils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.snappydb.DB;
import com.snappydb.KeyIterator;
import com.snappydb.SnappydbException;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;

public class DBImpl implements DB {
    private static final String LIB_NAME = "snappydb-native";
    private static final int LIMIT_MAX = Integer.MAX_VALUE - 8;

    private String dbPath;
    private Kryo kryo;

    static {
        System.loadLibrary(LIB_NAME);
    }

    public DBImpl(String path, Kryo... kryo) throws SnappydbException {
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
    public void destroy() throws SnappydbException {
        __destroy(dbPath);
    }

    @Override
    public boolean isOpen() throws SnappydbException {
        return __isOpen();
    }


    // ***********************
    // *       CREATE
    // ***********************
    @Override
    public void put(String key, String value) throws SnappydbException {
        checkArgs(key, value);

        __put(key, value);
    }

    @Override
    public void put(String key, Serializable value) throws SnappydbException {
        checkArgs(key, value);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kryo.register(value.getClass());

        Output output = new Output(stream);
        try {
            kryo.writeObject(output, value);
            output.close();

            __put(key, stream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new SnappydbException(e.getMessage());
        }
    }

    @Override
    public void put(String key, Object value) throws SnappydbException {
        checkArgs(key, value);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kryo.register(value.getClass());

        Output output = new Output(stream);
        try {
            kryo.writeObject(output, value);
            output.close();

            __put(key, stream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new SnappydbException(e.getMessage());
        }
    }

    @Override
    public void put(String key, byte[] value) throws SnappydbException {
        checkArgs(key, value);

        __put(key, value);
    }


    @Override
    public void put(String key, Serializable[] value) throws SnappydbException {
        checkArgs(key, value);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kryo.register(value.getClass());

        Output output = new Output(stream);
        try {
            kryo.writeObject(output, value);
            output.close();

            __put(key, stream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new SnappydbException("Kryo exception " + e.getMessage());
        }
    }

    @Override
    public void put(String key, Object[] value) throws SnappydbException {
        checkArgs(key, value);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        kryo.register(value.getClass());

        Output output = new Output(stream);
        try {
            kryo.writeObject(output, value);
            output.close();

            __put(key, stream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            throw new SnappydbException("Kryo exception " + e.getMessage());
        }
    }

    @Override
    public void putShort(String key, short val) throws SnappydbException {
        checkKey(key);

        __putShort(key, val);
    }


    @Override
    public void putInt(String key, int val) throws SnappydbException {
        checkKey(key);

        __putInt(key, val);
    }


    @Override
    public void putBoolean(String key, boolean val) throws SnappydbException {
        checkKey(key);

        __putBoolean(key, val);
    }

    @Override
    public void putDouble(String key, double val) throws SnappydbException {
        checkKey(key);

        __putDouble(key, val);
    }

    @Override
    public void putFloat(String key, float val) throws SnappydbException {
        checkKey(key);

        __putFloat(key, val);
    }

    @Override
    public void putLong(String key, long val) throws SnappydbException {
        checkKey(key);

        __putLong(key, val);
    }

    // ***********************
    // *      DELETE
    // ***********************

    @Override
    public void del(String key) throws SnappydbException {
        checkKey(key);

        __del(key);
    }

    // ***********************
    // *       RETRIEVE
    // ***********************

    @Override
    public <T extends Serializable> T get(String key, Class<T> className)
            throws SnappydbException {
        checkArgs(key, className);

        if (className.isArray()) {
            throw new SnappydbException(
                    "You should call getArray instead");
        }

        byte[] data = getBytes(key);

        kryo.register(className);

        Input input = new Input(data);
        try {
            return kryo.readObject(input, className);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SnappydbException("Maybe you tried to retrieve an array using this method ? " +
                    "please use getArray instead " + e.getMessage());
        } finally {
            input.close();
        }
    }

    @Override
    public <T> T getObject (String key, Class<T> className)
            throws SnappydbException {
        checkArgs(key, className);

        if (className.isArray()) {
            throw new SnappydbException(
                    "You should call getObjectArray instead");
        }

        byte[] data = getBytes(key);

        kryo.register(className);

        Input input = new Input(data);
        try {
            return kryo.readObject(input, className);

        } catch (Exception e) {
            e.printStackTrace();
            throw new SnappydbException("Maybe you tried to retrieve an array using this method ? " +
                    "please use getObjectArray instead " + e.getMessage());
        } finally {
            input.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> T[] getArray(String key, Class<T> className)
            throws SnappydbException {
        checkArgs(key, className);

        byte[] data = __getBytes(key);

        kryo.register(className);

        Input input = new Input(data);
        T[] array = (T[]) Array.newInstance(className, 0);

        try {
            return (T[]) kryo.readObject(input, array.getClass());

        } catch (Exception e) {
            e.printStackTrace();
            throw new SnappydbException("Maybe you tried to retrieve an array using this method " +
                    "? please use getArray instead " + e.getMessage());
        } finally {
            input.close();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] getObjectArray(String key, Class<T> className)
            throws SnappydbException {
        checkArgs(key, className);

        byte[] data = __getBytes(key);

        kryo.register(className);

        Input input = new Input(data);
        T[] array = (T[]) Array.newInstance(className, 0);

        try {
            return (T[]) kryo.readObject(input, array.getClass());

        } catch (Exception e) {
            e.printStackTrace();
            throw new SnappydbException("Maybe you tried to retrieve an array using this method " +
                    "? please use getArray instead " + e.getMessage());
        } finally {
            input.close();
        }
    }

    @Override
    public byte[] getBytes(String key) throws SnappydbException {
        checkKey(key);

        return __getBytes(key);
    }

    @Override
    public String get(String key) throws SnappydbException {
        checkKey(key);

        return __get(key);
    }

    @Override
    public short getShort(String key) throws SnappydbException {
        checkKey(key);

        return __getShort(key);
    }

    @Override
    public int getInt(String key) throws SnappydbException {
        checkKey(key);

        return __getInt(key);
    }

    @Override
    public boolean getBoolean(String key) throws SnappydbException {
        checkKey(key);

        return __getBoolean(key);
    }

    @Override
    public double getDouble(String key) throws SnappydbException {
        checkKey(key);

        return __getDouble(key);
    }

    @Override
    public long getLong(String key) throws SnappydbException {
        checkKey(key);

        return __getLong(key);
    }

    @Override
    public float getFloat(String key) throws SnappydbException {
        checkKey(key);

        return __getFloat(key);
    }

    //****************************
    //*      KEYS OPERATIONS
    //****************************
    @Override
    public boolean exists(String key) throws SnappydbException {
        checkKey(key);

        return __exists(key);
    }

    @Override
    public String[] findKeys(String prefix) throws SnappydbException {
        return findKeys(prefix, 0, LIMIT_MAX);
    }

    @Override
    public String[] findKeys(String prefix, int offset) throws SnappydbException {
        return findKeys(prefix, offset, LIMIT_MAX);
    }

    @Override
    public String[] findKeys(String prefix, int offset, int limit) throws SnappydbException {
        checkPrefix(prefix);
        checkOffsetLimit(offset, limit);

        return __findKeys(prefix, offset, limit);
    }

    @Override
    public int countKeys(String prefix) throws SnappydbException {
        checkPrefix(prefix);

        return __countKeys(prefix);
    }

    @Override
    public String[] findKeysBetween(String startPrefix, String endPrefix)
            throws SnappydbException {
        return findKeysBetween(startPrefix, endPrefix, 0, LIMIT_MAX);
    }

    @Override
    public String[] findKeysBetween(String startPrefix, String endPrefix, int offset)
            throws SnappydbException {
        return findKeysBetween(startPrefix, endPrefix, offset, LIMIT_MAX);
    }

    @Override
    public String[] findKeysBetween(String startPrefix, String endPrefix, int offset, int limit)
            throws SnappydbException {
        checkRange(startPrefix, endPrefix);
        checkOffsetLimit(offset, limit);

        return __findKeysBetween(startPrefix, endPrefix, offset, limit);
    }

    @Override
    public int countKeysBetween(String startPrefix, String endPrefix)
            throws SnappydbException {
        checkRange(startPrefix, endPrefix);

        return __countKeysBetween(startPrefix, endPrefix);
    }

    //***********************
    //*      ITERATORS
    //***********************
    @Override
    public KeyIterator allKeysIterator()
            throws SnappydbException {
        return new KeyIteratorImpl(this, __findKeysIterator(null, false), null, false);
    }

    @Override
    public KeyIterator allKeysReverseIterator()
            throws SnappydbException {
        return new KeyIteratorImpl(this, __findKeysIterator(null, true),  null, true);
    }

    @Override
    public KeyIterator findKeysIterator(String prefix)
            throws SnappydbException {
        return new KeyIteratorImpl(this, __findKeysIterator(prefix, false), null, false);
    }

    @Override
    public KeyIterator findKeysReverseIterator(String prefix)
            throws SnappydbException {
        return new KeyIteratorImpl(this, __findKeysIterator(prefix, true), null, true);
    }

    @Override
    public KeyIterator findKeysBetweenIterator(String startPrefix, String endPrefix)
            throws SnappydbException {
        return new KeyIteratorImpl(this, __findKeysIterator(startPrefix, false), endPrefix, false);
    }

    @Override
    public KeyIterator findKeysBetweenReverseIterator(String startPrefix, String endPrefix)
            throws SnappydbException {
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

    private void checkArgs (String key, Object value) throws SnappydbException {
        checkArgNotEmpty (key, "Key must not be empty");

        if (null == value) {
            throw new SnappydbException ("Value must not be empty");
        }
    }

    private void checkPrefix (String prefix) throws SnappydbException {
        checkArgNotEmpty (prefix, "Starting prefix must not be empty");
    }

    private void checkRange (String startPrefix, String endPrefix) throws SnappydbException {
        checkArgNotEmpty (startPrefix, "Starting prefix must not be empty");
        checkArgNotEmpty (startPrefix, "Ending prefix must not be empty");
    }

    private void checkKey (String key) throws SnappydbException {
        checkArgNotEmpty (key, "Key must not be empty");
    }

    private void checkArgNotEmpty (String arg, String errorMsg) throws SnappydbException {
        if (TextUtils.isEmpty(arg)) {
            throw new SnappydbException (errorMsg);
        }
    }

    private void checkOffsetLimit (int offset, int limit) throws SnappydbException {
        if (offset < 0) {
            throw new SnappydbException ("Offset must not be negative");
        }
        if (limit <= 0) {
            throw new SnappydbException ("Limit must not be 0 or negative");
        }
    }


    // native code
    private native void __close();

    private native void __open(String dbName) throws SnappydbException;

    private native void __destroy(String dbName) throws SnappydbException;

    private native boolean __isOpen() throws SnappydbException;

    private native void __put(String key, byte[] value) throws SnappydbException;

    private native void __put(String key, String value) throws SnappydbException;

    private native void __putShort(String key, short val) throws SnappydbException;

    private native void __putInt(String key, int val) throws SnappydbException;

    private native void __putBoolean(String key, boolean val) throws SnappydbException;

    private native void __putDouble(String key, double val) throws SnappydbException;

    private native void __putFloat(String key, float val) throws SnappydbException;

    private native void __putLong(String key, long val) throws SnappydbException;

    private native void __del(String key) throws SnappydbException;

    private native byte[] __getBytes(String key) throws SnappydbException;

    private native String __get(String key) throws SnappydbException;

    private native short __getShort(String key) throws SnappydbException;

    private native int __getInt(String key) throws SnappydbException;

    private native boolean __getBoolean(String key) throws SnappydbException;

    private native double __getDouble(String key) throws SnappydbException;

    private native long __getLong(String key) throws SnappydbException;

    private native float __getFloat(String key) throws SnappydbException;

    private native boolean __exists(String key) throws SnappydbException;

    private native String[] __findKeys (String prefix, int offset, int limit) throws SnappydbException;

    private native int __countKeys (String prefix) throws SnappydbException;

    private native String[] __findKeysBetween(String startPrefix, String endPrefix, int offset, int limit) throws SnappydbException;

    private native int __countKeysBetween(String startPrefix, String endPrefix) throws SnappydbException;

    native long __findKeysIterator(String prefix, boolean reverse) throws SnappydbException;

    native String[] __iteratorNextArray(long ptr, String endPrefix, boolean reverse, int max) throws SnappydbException;

    native boolean __iteratorIsValid(long ptr, String endPrefix, boolean reverse);

    native void __iteratorClose(long ptr);
}
