package com.jinjunhang.contract.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jinjunhang.contract.model.Approval;

public class ApprovalNotificationStore {
    private DBOpenHelper dbOpenHelper;
    private static ApprovalNotificationStore instance = null;

    public ApprovalNotificationStore(Context context) {
        this.dbOpenHelper = new DBOpenHelper(context);
    }

    public synchronized static ApprovalNotificationStore getInstance(Context ctx) {
        if (null == instance) {
            instance = new ApprovalNotificationStore(ctx);
        }
        return instance;
    }

    public void save(Approval approval, int badge) {

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("badge", badge);

        if (getCount() > 0) {
            db.update("notification", values, "", new String[]{});
        } else {
            db.insert("notification", null, values);
        }
    }

    public int getBadge() {
        if (getCount() == 0) {
            return 0;
        } else {
            SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select badge from notification", null);
            try {
                cursor.moveToFirst();
                return cursor.getInt(0);
            } finally {
                cursor.close();
            }
        }
    }


    public void deleteAll() {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        db.delete("notification", "", null);
    }


    public int getCount() {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from notification", null);
        try {
            cursor.moveToFirst();
            return cursor.getInt(0);
        } finally {
            cursor.close();
        }
    }
}
