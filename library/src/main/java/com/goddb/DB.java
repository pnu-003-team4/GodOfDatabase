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

package com.goddb;

import com.esotericsoftware.kryo.Kryo;

import org.json.JSONArray;
import org.json.JSONObject;

public interface DB {
    //******************************************************************************************************************
    //*      DB MANAGEMENT
    //******************************************************************************************************************

    /**
     * Closes database.
     *
     *  @throws GoddbException if exception
     */
    void close() throws GoddbException;

    /**
     * Destroys database
     *
     *  @throws GoddbException if exception
     */
    void destroy() throws GoddbException;

    /**
     * Checks if database is open.
     *
     * @return {@code true} if database is open.
     */
    boolean isOpen() throws GoddbException;

    //******************************************************************************************************************
    //*      INSERT
    //******************************************************************************************************************

    /**
     * Puts the object to the path.
     *
     * @param path not null.
     * @param object not null.
     * @throws GoddbException if the key or data is null.
     */
    void put(String path, JSONObject object) throws GoddbException;


    /**
     * Puts the object array to the path.
     *
     * @param path not null.
     * @param objects not null.
     * @throws GoddbException if the key or data is null.
     */
    void put(String path, JSONArray objects) throws GoddbException;

    //******************************************************************************************************************
    //*      DELETE
    //******************************************************************************************************************
    /**
     * Deletes the objects in path following the wildcard and codition.
     *
     * @param path not null.
     * @param condition allow null.
     * @throws GoddbException if the key is null.
     */
    JSONArray del(String path, String condition) throws GoddbException;

    /**
     * Deletes the path following the wildcard and codition.
     *
     * @param path      not null.
     * @throws GoddbException if the key is null.
     */
    JSONArray deldir(String path) throws GoddbException;

    //******************************************************************************************************************
    //*      UPDATE
    //******************************************************************************************************************
    /**
     * Updates the objects in path following the wildcard and codition.
     *
     * @param path not null.
     * @param condition allow null.
     * @throws GoddbException if the key is null.
     */
    void update(String path, String condition, String modData) throws GoddbException;


    //******************************************************************************************************************
    //*      RETRIEVE
    //******************************************************************************************************************
    /**
     * Gets the object array in path following the wildcard and condition.
     *
     * @param path not null.
     * @param condition allow null.
     * @throws GoddbException if the path is null.
     */
    JSONArray get(String path, String condition) throws GoddbException;

    //******************************************************************************************************************
    //*      KEYS OPERATIONS || TODO: STUDY - How to use these functions?
    //******************************************************************************************************************
    boolean exists(String key) throws GoddbException;

    String[] findKeys(String prefix) throws GoddbException;

    String[] findKeys(String prefix, int offset) throws GoddbException;

    String[] findKeys(String prefix, int offset, int limit) throws GoddbException;

    int countKeys(String prefix) throws GoddbException;

    String[] findKeysBetween(String startPrefix, String endPrefix) throws GoddbException;

    String[] findKeysBetween(String startPrefix, String endPrefix, int offset) throws GoddbException;

    String[] findKeysBetween(String startPrefix, String endPrefix, int offset, int limit) throws GoddbException;

    int countKeysBetween(String startPrefix, String endPrefix) throws GoddbException;

    //******************************************************************************************************************
    //*      ITERATORS
    //******************************************************************************************************************
    KeyIterator allKeysIterator() throws GoddbException;

    KeyIterator allKeysReverseIterator() throws GoddbException;

    KeyIterator findKeysIterator(String prefix) throws GoddbException;

    KeyIterator findKeysReverseIterator(String prefix) throws GoddbException;

    KeyIterator findKeysBetweenIterator(String startPrefix, String endPrefix) throws GoddbException;

    KeyIterator findKeysBetweenReverseIterator(String startPrefix, String endPrefix) throws GoddbException;

    //******************************************************************************************************************
    //*      KRYO SERIALIZATION
    //******************************************************************************************************************

    /**
     * @return an instance of {@link Kryo}. This is to allow users to customize the {@link Kryo} instance.
     */
    Kryo getKryoInstance ();
}
