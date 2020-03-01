package com.fahmisbas.simplify.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HelperDB extends SQLiteOpenHelper {


    public HelperDB(@Nullable Context context) {
        super(context, "simplify", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_DATABASE_SIMPLIFY_NOTE = " CREATE TABLE " +
                ContractDB.EntryDB.TABLE_NAME + " (" +
                ContractDB.EntryDB.COLUMN_TITLE + " TEXT NOT NULL, " +
                ContractDB.EntryDB.COLUMN_NOTE + " TEXT NOT NULL, " +
                ContractDB.EntryDB._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractDB.EntryDB.TIMESTAMP + " TIMESTMAP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_DATABASE_SIMPLIFY_NOTE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ContractDB.EntryDB.TABLE_NAME);
        onCreate(db);
    }
}
