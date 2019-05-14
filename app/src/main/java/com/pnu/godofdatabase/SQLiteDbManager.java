package com.pnu.godofdatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class SQLiteDbManager extends SQLiteOpenHelper {


    public SQLiteDbManager(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블을 생성한다.
        // create table 테이블명 (컬럼명 타입 옵션);
        db.execSQL("CREATE TABLE MYLIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER);");
//        db.execSQL("CREATE TABLE TEST01( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, number INTEGER);");
//        db.execSQL("CREATE TABLE TEST02( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, number INTEGER);");
//        db.execSQL("CREATE TABLE TEST03( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, number INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String _query) {
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(_query);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    public void update(String _query) {
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(_query);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void delete(String _query) {
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(_query);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String PrintData() {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from MYLIST", null);
        while (cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + " : Name "
                    + cursor.getString(1)
                    + ", number = "
                    + cursor.getInt(2)
                    + "\n";
        }

        return str;
    }

    public String ConditionPrintData(String _query) {
        SQLiteDatabase db = getReadableDatabase();
        String str = "";
        String compare = _query.substring(7,_query.indexOf("from"));

        if (compare.equals("name")) {
            Cursor cursor = db.rawQuery("select * from MYLIST", null);

            while (cursor.moveToNext()) {
                str += cursor.getInt(0)
                        + ", name = "
                        + cursor.getString(1)
                        + "\n";
            }
        }
        else if (compare.equals("price")) {
            Cursor cursor = db.rawQuery("select * from MYLIST", null);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                str += cursor.getInt(0)
                        + ", price = "
                        + cursor.getInt(2)
                        + "\n";
            }
        }

        return str;
    }

    public void select(String _query){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }
}
