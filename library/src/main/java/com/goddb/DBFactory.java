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

import android.content.Context;

import com.esotericsoftware.kryo.Kryo;
import com.goddb.internal.DBImpl;

import java.io.File;
import java.io.IOException;

public class DBFactory {
    private final static String DEFAULT_DBNAME = "goddb";

    /**
     * Return the Database with the given folder and name, if it doesn't exist create it
     *
     * @param folder the folder of the db file will be stored
     * @param dbName database file name
     * @param kryo optional custom instance of {@link com.esotericsoftware.kryo.Kryo} serializer
     * @return Database handler {@link DB}
     * @throws GoddbException if exception
     */
    public static DB open(Context ctx, String folder, String dbName, Kryo... kryo) throws GoddbException, IOException, ClassNotFoundException {
        String dbFilePath = folder + File.separator + dbName;
        return new DBImpl(ctx, dbFilePath, kryo);
    }

    /**
     * Return the Database with the given name, if it doesn't exist create it
     *
     * @param ctx    context
     * @param dbName database file name
     * @param kryo optional custom instance of {@link com.esotericsoftware.kryo.Kryo} serializer
     * @return Database handler {@link DB}
     * @throws GoddbException if exception
     */
    public static DB open(Context ctx, String dbName, Kryo... kryo) throws GoddbException, IOException, ClassNotFoundException {
        return open(ctx, ctx.getFilesDir().getAbsolutePath(), dbName, kryo);
    }

    /**
     * Return the Database with the default name {@link DBFactory#DEFAULT_DBNAME}, if it doesn't exist create it
     *
     * @param ctx context
     * @param kryo optional custom instance of {@link com.esotericsoftware.kryo.Kryo} serializer
     * @return Database handler {@link DB}
     * @throws GoddbException if exception
     */
    public static DB open(Context ctx, Kryo... kryo) throws GoddbException, IOException, ClassNotFoundException {
        return open(ctx, DEFAULT_DBNAME, kryo);
    }

}

