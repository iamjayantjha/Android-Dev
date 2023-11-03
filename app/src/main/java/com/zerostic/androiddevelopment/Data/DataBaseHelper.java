package com.zerostic.androiddevelopment.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "pregrad.db";
    public static final String TABLE_NAME = "Students";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "EMAIL";
    public static final String COL_4 = "PHONE";
    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, EMAIL TEXT, PHONE TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean insertData(String name, String email, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( COL_2, name );
        contentValues.put( COL_3, email );
        contentValues.put( COL_4, phone );
        long result = db.insert( TABLE_NAME, null, contentValues );
        return result != -1;
    }

    public Cursor readData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );
    }

    public Cursor readDataWhereNameIs(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery( "SELECT * FROM " + TABLE_NAME + " WHERE NAME = ?", new String[]{name} );
    }

    public boolean updateData(String id, String name, String email, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put( COL_1, id );
        contentValues.put( COL_2, name );
        contentValues.put( COL_3, email );
        contentValues.put( COL_4, phone );
        int result = db.update( TABLE_NAME, contentValues, "ID = ?", new String[]{id} );
        return result > 0;
    }

    public int deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete( TABLE_NAME, "ID = ?", new String[]{id} );
    }

}
