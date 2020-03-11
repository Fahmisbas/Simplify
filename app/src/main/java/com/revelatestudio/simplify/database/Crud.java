package com.revelatestudio.simplify.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.revelatestudio.simplify.activities.MainActivity;

public class Crud {

    private SQLiteDatabase database;


    public Crud(Context context){
        HelperDB helperDB = new HelperDB(context);
        database = helperDB.getWritableDatabase();
    }


    public void deleteData(long id) {
        database.delete(ContractDB.EntryDB.TABLE_NAME,
                ContractDB.EntryDB._ID + "=" + id, null);
        MainActivity.adapter.swapCursor(readData());
    }

    public void addData(String title,String note) {
        ContentValues cv = new ContentValues();
        cv.put(ContractDB.EntryDB.COLUMN_TITLE, title);
        cv.put(ContractDB.EntryDB.COLUMN_NOTE, note);
        database.insert(ContractDB.EntryDB.TABLE_NAME, null, cv);
        MainActivity.adapter.swapCursor(readData());
    }

    /*public void updateData(long id, String title, String note) {
        ContentValues cv = new ContentValues();
        cv.put(ContractDB.EntryDB.COLUMN_TITLE, title);
        cv.put(ContractDB.EntryDB.COLUMN_NOTE, note);
        database.update(ContractDB.EntryDB.TABLE_NAME, cv, "_id = ?", new String[]{String.valueOf(id)});
        MainActivity.adapter.swapCursor(readData());
    }*/

    public Cursor readData() {
        return database.query(ContractDB.EntryDB.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ContractDB.EntryDB.TIMESTAMP + " DESC");
    }

}
