package edu.gatech.teamraid.ratastic.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by karshinlin on 9/22/17.
 */

public class DataLogger extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Ratastic.db";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + "Credentials";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + "Credentials" + " (" +
                    "Username" + " TEXT PRIMARY KEY," +
                    "Password" + " TEXT)";
    public DataLogger(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
