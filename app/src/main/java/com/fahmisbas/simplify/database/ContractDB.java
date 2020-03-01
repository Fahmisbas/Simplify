package com.fahmisbas.simplify.database;

import android.provider.BaseColumns;

public class ContractDB {

    ContractDB(){
    }

    public static class EntryDB implements BaseColumns{
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_TITLE = "ctitle";
        public static final String COLUMN_NOTE = "cnote";
        public static final String TIMESTAMP = "time";
    }

}
