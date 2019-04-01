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

package com.snappydb;

import com.esotericsoftware.kryo.Kryo;

import org.json.JSONObject;

public interface DB {
    //******************************************************************************************************************
    //*      DB MANAGEMENT
    //******************************************************************************************************************

    /**
     * Closes database.
     *
     *  @throws SnappydbException
     */
    void close ()  throws SnappydbException;

    /**
     * Destroys database
     *
     *  @throws SnappydbException
     */
    void destroy ()  throws SnappydbException;

    /**
     * Checks if database is open.
     *
     * @return {@code true} if database is open.
     */
    boolean isOpen ()  throws SnappydbException;

    //******************************************************************************************************************
    //*      INSERT
    //******************************************************************************************************************

    /**
     * Puts the object to the path.
     *
     * @param path not null.
     * @param object not null.
     * @throws SnappydbException if the key or data is null.
     */
    void put (String path, JSONObject object) throws SnappydbException;


    /**
     * Puts the object array to the path.
     *
     * @param path not null.
     * @param objects not null.
     * @throws SnappydbException if the key or data is null.
     */
    void put (String path, JSONObject[] objects) throws SnappydbException;

    //******************************************************************************************************************
    //*      DELETE
    //******************************************************************************************************************
    /**
     * Deletes the objects in path following the wildcard and codition.
     *
     * @param path not null.
     * @param wildcard allow null.
     * @param condition allow null.
     * @throws SnappydbException if the key is null.
     */
    void del (String path, String wildcard, String condition)  throws SnappydbException;

    //******************************************************************************************************************
    //*      RETRIEVE
    //******************************************************************************************************************
    /**
     * Gets the object array in path following the wildcard and condition.
     *
     * @param key not null.
     * @param wildcard allow null.
     * @param condition allow null.
     * @throws SnappydbException if the key is null.
     */
    JSONObject[] get(String key, String wildcard, String condition)  throws SnappydbException;

    //******************************************************************************************************************
    //*      KEYS OPERATIONS || TODO: STUDY - How to use these functions?
    //******************************************************************************************************************
    boolean exists (String key) throws SnappydbException;

    String[] findKeys(String prefix) throws SnappydbException;
    String[] findKeys(String prefix, int offset) throws SnappydbException;
    String[] findKeys(String prefix, int offset, int limit) throws SnappydbException;

    int countKeys(String prefix) throws SnappydbException;

    String[] findKeysBetween(String startPrefix, String endPrefix) throws SnappydbException;
    String[] findKeysBetween(String startPrefix, String endPrefix, int offset) throws SnappydbException;
    String[] findKeysBetween(String startPrefix, String endPrefix, int offset, int limit) throws SnappydbException;

    int countKeysBetween(String startPrefix, String endPrefix) throws SnappydbException;

    //******************************************************************************************************************
    //*      ITERATORS
    //******************************************************************************************************************
    KeyIterator allKeysIterator() throws SnappydbException;
    KeyIterator allKeysReverseIterator() throws SnappydbException;

    KeyIterator findKeysIterator(String prefix) throws SnappydbException;
    KeyIterator findKeysReverseIterator(String prefix) throws SnappydbException;

    KeyIterator findKeysBetweenIterator(String startPrefix, String endPrefix) throws SnappydbException;
    KeyIterator findKeysBetweenReverseIterator(String startPrefix, String endPrefix) throws SnappydbException;

    //******************************************************************************************************************
    //*      KRYO SERIALIZATION
    //******************************************************************************************************************

    /**
     * @return an instance of {@link Kryo}. This is to allow users to customize the {@link Kryo} instance.
     */
    Kryo getKryoInstance ();
}
