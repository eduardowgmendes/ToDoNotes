package br.com.jet.app.todonotes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import br.com.jet.app.todonotes.model.Task;

public class TaskDatabase extends SQLiteOpenHelper {

    private static final String TAG = "database";

    private Context context;
    private static final String NAME = "TasksDatabase";
    private static final int VERSION = 1;

    public TaskDatabase(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TASKS (_ID INTEGER PRIMARY KEY AUTOINCREMENT, PRIORITY INTEGER, TITLE VARCHAR(60), CONTENT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TASKS");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO - IMPLEMENT UPGRADE POLICIES
    }

    public void insert(Task task) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        try {
            values.put("PRIORITY", task.getPriority());
            values.put("TITLE", task.getTitle());
            values.put("CONTENT", task.getContent());
            database.insert("TASKS", null, values);
        } finally {
            database.close();
        }
    }

    public void update(Task task) {
        SQLiteDatabase database = getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("PRIORITY", task.getPriority());
            values.put("TITLE", task.getTitle());
            values.put("CONTENT", task.getContent());
            database.update("TASKS", values, "_ID = ?", new String[]{String.valueOf(task.getId())});
        } finally {
            database.close();
        }
    }

    public void delete(long id) {
        SQLiteDatabase database = getWritableDatabase();
        try {
            database.delete("TASKS", "_ID = ?", new String[]{String.valueOf(id)});
        } finally {
            database.close();
        }
    }

    public void clear() {
        SQLiteDatabase database = getWritableDatabase();
        try {
            database.execSQL("DELETE FROM TASKS");
        } finally {
            database.close();
        }
    }

    public ArrayList<Task> queryAll() {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            Cursor cursor = database.query("TASKS", null, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    tasks.add(new Task(cursor.getLong(cursor.getColumnIndex("_ID")), cursor.getInt(cursor.getColumnIndex("PRIORITY")), cursor.getString(cursor.getColumnIndex("TITLE")), cursor.getString(cursor.getColumnIndex("CONTENT"))));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return tasks;

        } finally {
            database.close();
        }
    }

    public ArrayList<Task> queryBy(String content) {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            Cursor cursor = database.query("TASKS", null, "TITLE OR CONTENT LIKE ?", new String[]{"%" + content + "%"}, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    tasks.add(new Task(cursor.getLong(cursor.getColumnIndex("_ID")), cursor.getInt(cursor.getColumnIndex("PRIORITY")), cursor.getString(cursor.getColumnIndex("TITLE")), cursor.getString(cursor.getColumnIndex("CONTENT"))));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return tasks;
        } finally {
            database.close();
        }
    }

    public ArrayList<Task> queryBy(int priority) {
        SQLiteDatabase database = getReadableDatabase();
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            Cursor cursor = database.query("TASKS", null, "PRIORITY = ?", new String[]{String.valueOf(priority)}, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    tasks.add(new Task(cursor.getLong(cursor.getColumnIndex("_ID")), cursor.getInt(cursor.getColumnIndex("PRIORITY")), cursor.getString(cursor.getColumnIndex("TITLE")), cursor.getString(cursor.getColumnIndex("CONTENT"))));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return tasks;
        } finally {
            database.close();
        }
    }

}
