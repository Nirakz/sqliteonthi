package com.example.onthicuoiki;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dbname";

    private static final String TABLE_NAME = "tbperson";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_MAIL = "email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String value = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT)", TABLE_NAME, COLUMN_ID, COLUMN_NAME, COLUMN_MAIL);
        db.execSQL(value);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String value = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(value);
        onCreate(db);
    }
    public long create(String name, String mail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MAIL, mail);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public int update(long id, String name, String mail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MAIL, mail);
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void delete(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Person getValue(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_MAIL}, COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Person person = new Person();
        person.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        person.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
        person.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_MAIL)));
        cursor.close();
        return person;
    }

    public List<Person> getAll() {
        List<Person> list = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Person person = new Person();
                person.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                person.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                person.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_MAIL)));
                list.add(person);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }

}