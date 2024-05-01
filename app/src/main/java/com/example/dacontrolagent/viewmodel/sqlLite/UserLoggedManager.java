package com.example.dacontrolagent.viewmodel.sqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dacontrolagent.domain.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserLoggedManager {
    private final DatabaseHelper dbHelper;

    public UserLoggedManager(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public void saveUserLogged(String email, String password) {
        new Thread(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("email", email);
            values.put("password", password);

            db.insert("userLogged", null, values);
        }).start();

    }

    public Optional<User> getUserLogged() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<User> users = new ArrayList<>();

        Cursor cursor = db.query("userLogged", new String[]{"id", "email", "password"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
            users.add(new User(email, password));
        }
        cursor.close();
        return users.stream().findFirst();
    }

    public void deleteUser() {
        new Thread(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("userLogged", null, null);
        }).start();
    }
}
