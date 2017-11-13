package edu.gatech.teamraid.ratastic.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite class.
 * UPDATES:
 * DATE     | DEV    | DESCRIPTION
 *
 */

public class DataLogger extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "Ratastic.db";
    public static final String DATABASE_TABLE_NAME = "RatSighting";
    public static final String Lock = "Lock";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
                    "UID" + " TEXT PRIMARY KEY," +
                    "Created_Date" + " DATETIME," +
                    "Location_Type" + " TEXT," +
                    "Incident_Zip" + " INT," +
                    "Incident_Address" + " TEXT," +
                    "City" + " TEXT," +
                    "Borough" + " TEXT," +
                    "Latitude" + " FLOAT," +
                    "Longitude" + " FLOAT)";

    /**
     * Method to create a new DB connection
     * @param context application that requests the new connection
     */
    public DataLogger(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    private static DataLogger instance;

    /**
     * Getting the singleton instance of the open database connection
     * @param context application requesting connection
     * @return returns the DataLogger object with the connection
     */
    public static synchronized DataLogger getHelper(Context context)
    {
        if (instance == null)
            instance = new DataLogger(context);

        return instance;
    }

    /**
     * creates new database in the Android application
     * @param db db to create
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        // create the index for our INSERT OR REPLACE INTO statement.
        // this acts as the WHERE UID="uid input" AND Created_Date="created date input" AND ...so on...
        // if that WHERE clause is true, then that tuple in the database is REPLACED
        // ELSE, what's in the database will remain and the input will be INSERTED (new record)
//        String INDEX = "CREATE UNIQUE INDEX locations_index ON "
//                + "RatSighting" + " (UID, Created_Date, Location_Type, Incident_Zip, Incident_Address," +
//                "City, Borough, Latitude, Longitude)";
//        db.execSQL(INDEX);
    }

    /**
     * If new db version is created (schema change)
     * @param db db being modified
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    /**
     * If db needs to be downgraded
     * @param db db to be downgraded
     * @param oldVersion old version number
     * @param newVersion new version number
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * method to add new rat report to database
     * @param db db which holds the table
     * @param uid uid
     * @param createdDate date
     * @param locType type
     * @param incidentZip zip
     * @param incidentAddress address
     * @param city city
     * @param borough borough
     * @param latitude latitude
     * @param longitude longitude
     */
    public void writeNormal(SQLiteDatabase db, String uid, String createdDate, String locType, String incidentZip,
                      String incidentAddress, String city, String borough, String latitude, String longitude) {
        ContentValues values = new ContentValues();
        values.put("UID", uid);
        values.put("Created_Date", createdDate);
        values.put("Location_Type", locType);
        values.put("Incident_Zip", incidentZip);
        values.put("Incident_Address", incidentAddress);
        values.put("City", city);
        values.put("Borough", borough);
        values.put("Latitude", latitude);
        values.put("Longitude", longitude);
        db.insert(DATABASE_TABLE_NAME, null, values);
    }
}
