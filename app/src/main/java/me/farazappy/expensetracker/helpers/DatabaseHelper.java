package me.farazappy.expensetracker.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.farazappy.expensetracker.models.Day;
import me.farazappy.expensetracker.models.Item;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "expense_tracker";

    private static final String TABLE_DAYS = "days";
    private static final String TABLE_ITEMS = "items";

    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_NAME = "name";

    private static final String KEY_COST = "cost";
    private static final String KEY_DAY_ID = "day_id";

    private static final String CREATE_TABLE_DAYS = "CREATE TABLE `" + TABLE_DAYS + "` ( `" + KEY_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, `" + KEY_NAME + "` VARCHAR(255) NOT NULL , `" + KEY_CREATED_AT + "` DATETIME )";
    private static final String CREATE_TABLE_ITEMS = "CREATE TABLE `" + TABLE_ITEMS + "` ( `" + KEY_ID + "` INTEGER PRIMARY KEY AUTOINCREMENT, `" + KEY_NAME + "` VARCHAR(255) NOT NULL , `" + KEY_COST + "` DOUBLE , `" + KEY_DAY_ID + "` INT , `" + KEY_CREATED_AT + "` DATETIME )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DAYS);
        db.execSQL(CREATE_TABLE_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAYS);

        onCreate(db);
    }

    public Day createDay(Day day) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, day.getName());
        values.put(KEY_CREATED_AT, getDateTime());

        long dayId = db.insert(TABLE_DAYS, null, values);

        db.close();

        return getDay(dayId);
    }

    public Item createItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, item.getName());
        values.put(KEY_COST, item.getCost());
        values.put(KEY_DAY_ID, item.getDayId());
        values.put(KEY_CREATED_AT, getDateTime());

        long itemId = db.insert(TABLE_ITEMS, null, values);

        db.close();

        return getItem(itemId);
    }

    public List<Day> getAllDays() {
        List<Day> days = new ArrayList<Day>();

        String selectQuery = "SELECT * FROM " + TABLE_DAYS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                Day d = new Day();
                d.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                d.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                d.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                days.add(d);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return days;
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                Item i = new Item();
                i.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                i.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                i.setCost(c.getDouble(c.getColumnIndex(KEY_COST)));
                i.setDayId(c.getInt(c.getColumnIndex(KEY_ID)));
                i.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                items.add(i);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return items;
    }

    public int updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_COST, item.getCost());

        return db.update(TABLE_ITEMS, values, KEY_ID + " = ?", new String[]{ String.valueOf(item.getId()) });

    }

    public void deleteItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?", new String[]{ String.valueOf(itemId) });
    }

    public List<Item> getAllItemsForDay(int dayId) {
        List<Item> items = new ArrayList<Item>();

        String selectQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE `" + KEY_DAY_ID + "` = '" + dayId + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                Item i = new Item();
                i.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                i.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                i.setCost(c.getDouble(c.getColumnIndex(KEY_COST)));
                i.setDayId(c.getInt(c.getColumnIndex(KEY_ID)));
                i.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                items.add(i);
            } while (c.moveToNext());
        }

        c.close();
        db.close();

        return items;
    }


    public Day getDay(long dayId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Day d = new Day();

        String selectQuery = "SELECT * FROM " + TABLE_DAYS + " WHERE `id` = " + dayId;

        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            d.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            d.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            d.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }

        c.close();
        db.close();

        return d;
    }

    private Item getItem(long itemId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Item i = new Item();

        String selectQuery = "SELECT * FROM " + TABLE_ITEMS + " WHERE `id` = " + itemId;

        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            i.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            i.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            i.setCost(c.getDouble(c.getColumnIndex(KEY_COST)));
            i.setDayId(c.getInt(c.getColumnIndex(KEY_ID)));
            i.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        }

        c.close();
        db.close();

        return i;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
