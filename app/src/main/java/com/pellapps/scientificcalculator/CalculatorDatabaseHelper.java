package com.pellapps.scientificcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalculatorDatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "calculator.db";
        private static final int DATABASE_VERSION = 1;
        public static final String HISTORY_TABLE_NAME = "history";
        public static final String HISTORY_COLUMN_ID = "_id";
        public static final String HISTORY_COLUMN_EXPRESSION = "expression";
        public static final String HISTORY_COLUMN_RESULT = "result";
        private static final String HISTORY_TABLE_CREATE =
                "CREATE TABLE " + HISTORY_TABLE_NAME + " (" +
                        HISTORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        HISTORY_COLUMN_EXPRESSION + " TEXT, " +
                        HISTORY_COLUMN_RESULT + " TEXT);";

        public CalculatorDatabaseHelper(Context context) {
            super(context, DATABASE_NAME , null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(HISTORY_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE_NAME);
            onCreate(db);
        }

        public boolean insertHistory(String expression, String result) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(HISTORY_COLUMN_EXPRESSION, expression);
            contentValues.put(HISTORY_COLUMN_RESULT, result);
            db.insert(HISTORY_TABLE_NAME, null, contentValues);
            return true;
        }

        public Cursor getData(){
            SQLiteDatabase db = this.getReadableDatabase();
            return db.rawQuery("SELECT * FROM " + HISTORY_TABLE_NAME, null);
        }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + HISTORY_TABLE_NAME);

    }
}

