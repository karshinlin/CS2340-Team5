package edu.gatech.teamraid.ratastic;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.gatech.teamraid.ratastic.Model.DataLogger;
import edu.gatech.teamraid.ratastic.Model.Location;
import edu.gatech.teamraid.ratastic.Model.RatSighting;
import edu.gatech.teamraid.ratastic.Model.User;

import static edu.gatech.teamraid.ratastic.Model.DataLogger.DATABASE_TABLE_NAME;
import static edu.gatech.teamraid.ratastic.Model.DataLogger.Lock;

/**
 * Login Page for Application. Linked to activity_login.xml

 * SQLite class.
 * UPDATES:
 * DATE     | DEV    | DESCRIPTION
 * 10/1/17:  KLIN     Created.
 * 10/9/17:  KLIN     Configured Firebase database usage to capture userType
 *
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ListView mainList;
    private ArrayAdapter<RatSighting> mainAdapter;

    TextView countSightings;

    private DatePickerDialog fromDateDialog;
    private DatePickerDialog toDateDialog;
    private EditText fromDateEditTxt;
    private EditText toDateEditTxt;

    private SimpleDateFormat dateFormatter;

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countSightings = (TextView) findViewById(R.id.countSightings);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mAuth.signOut();
                startActivity(intent);
            }
        });
        Button reportSighting = (Button) findViewById(R.id.report);
        reportSighting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ReportRatSightingActivity.class);
                startActivity(i);
            }
        });

        Button mapBtn = (Button) findViewById(R.id.mapButton);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchList = new Intent(MainActivity.this, MapsActivity.class);
                MainActivity.this.startActivity(switchList);
            }
        });

        TextView text = (TextView) findViewById(R.id.userType);
        if (User.currentUser != null && User.currentUser.getUserType() != null) text.setText("Hello " + User.currentUser.getUserType().toString());

        Button loadCsvBtn = (Button) findViewById(R.id.loadCsv);
        loadCsvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countSightings.setText("Number of Sightings: " + loadDBfromCSV());
                loadListFromDb();
            }
        });

//        DataLogger dbHelper = new DataLogger(this);
//        dbHelper.getReadableDatabase();

        //creates the main ListView shown upon login

        mainList = (ListView)findViewById(R.id.mainListView);
        mainAdapter = new ArrayAdapter<RatSighting>(this, R.layout.activity_listview, R.id.listTextView, RatSighting.ratSightingArray);
        mainList.setAdapter(mainAdapter);
//        mainAdapter = new LinkedHashMapAdapter<String, RatSighting>(this, R.layout.activity_listview, R.id.listTextView, RatSighting.ratSightingArray);
        //sets the onItemClickListener correctly
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, SightingListActivity.class);
                intent.putExtra("RatSighting", RatSighting.ratSightingArray.get(position));
                startActivity(intent);
            }
        });
        synchronized(Lock) {
            int loaded = loadListFromDb();
            countSightings.setText("Number of Sightings: " + loaded);
        }
        //countSightings.setText("Number of Sightings: " + RatSighting.ratSightingArray.size());

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        fromDateEditTxt = (EditText) findViewById(R.id.fromDate);
        fromDateEditTxt.requestFocus();
        fromDateEditTxt.setInputType(InputType.TYPE_NULL);


        toDateEditTxt = (EditText) findViewById(R.id.toDate);
        toDateEditTxt.setInputType(InputType.TYPE_NULL);
        setDateTimeField();

        Button filterOnDate = (Button) findViewById(R.id.filterOnDate);
        filterOnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countSightings.setText("Number of Sightings: " + loadListFromDb(fromDateEditTxt.getText().toString(), toDateEditTxt.getText().toString()));
            }
        });

    }
    private void setDateTimeField() {
        fromDateEditTxt.setOnClickListener(this);
        toDateEditTxt.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        fromDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEditTxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEditTxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    class AsyncLoadDBTask extends AsyncTask<String, String, String> {
        DataLogger databaseHandler;
        String type;
        int count;

        protected AsyncLoadDBTask(String task) {
            this.databaseHandler = new DataLogger(MainActivity.this);
            type = task;
            count = 0;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... url) {
            if (type.equals("LoadList")) {
                try {
                    synchronized (Lock) {
                        loadListFromDb();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (type.equals("LoadDB")) {
                try {
                    synchronized (Lock) {
                        count = loadDBfromCSV();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (type.equals("LoadDB_List")) {
                try {
                    synchronized (Lock) {
                        count = loadDBfromCSV();
                    }
                    synchronized (Lock) {
                        loadListFromDb();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        protected void onPostExecute(String unused) {
            countSightings.setText(count + "");
            //load arraylist
            mainAdapter.notifyDataSetChanged();
        }
    }


    public int loadDBfromCSV() {
        DataLogger dbHelper = DataLogger.getHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        try {
            String sql = "INSERT INTO " + DATABASE_TABLE_NAME + " ( UID, Created_Date, Location_Type, " +
                    "Incident_Zip, Incident_Address, City, Borough, Latitude, Longitude ) VALUES (?, ?, " +
                    "?, ?, ?, ?, ?, ?, ? )";
            db.beginTransaction();
            SQLiteStatement stmt = db.compileStatement(sql);

            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.ratsightings)));
            String[] nextLine;
            count = 0;
            long successful = 0;
            while ((nextLine = reader.readNext()) != null && count < 100) {
                if ((nextLine[0].equals("Unique Key") || nextLine[0].equals(""))) {
                    continue;
                }
                String UID = nextLine[0];
                if (nextLine[49].equals("") || nextLine[50].equals("")) {
                    continue;
                }
                float lat = Float.parseFloat(nextLine[49]);
                float lng = Float.parseFloat(nextLine[50]);
                String createdDate = nextLine[1];
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
                try {
                    Date date = format.parse(createdDate);
                    format = new SimpleDateFormat("yyyy-MM-dd");
                    createdDate = format.format(date);
                } catch (ParseException ex) {
                    //Logger.getLogger(Prime.class.getName()).log(Level.SEVERE, null, ex);
                    createdDate = null;
                }

                String locationType = nextLine[7];
                String incidentZip = nextLine[8];
                String incidentAddress = nextLine[9];
                String city = nextLine[16];
                String borough = nextLine[23];
                Location ratLocation = new Location(locationType, incidentZip,
                        incidentAddress, city, borough, lat, lng);
                if (!RatSighting.ratSightingHashMap.containsKey(UID)) {
                    RatSighting.ratSightingArray.add(new RatSighting(UID, createdDate, ratLocation));
                }
                count++;
                String[] args = {UID, createdDate, locationType, incidentZip, incidentAddress, city,
                        borough, lat+"", lng+""};
                stmt.bindAllArgsAsStrings(args);
                successful = stmt.executeInsert();
                if (successful < 0) {
                    break;
                }
                stmt.clearBindings();

            }
            if (successful < 0) {
                throw new IOException("Unsuccessful Entry");
            }
            db.setTransactionSuccessful();


        } catch (IOException e) {
            //Couldn't load data
            e.printStackTrace();
            db.close();
        } finally {
            db.endTransaction();
            db.close();
            return count;
        }
    }

    public int loadListFromDb() {
        DataLogger dbHelper = DataLogger.getHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransactionNonExclusive();
        String[] projection = {
                "UID",
                "Created_Date",
                "Location_Type",
                "Incident_Zip",
                "Incident_Address",
                "City",
                "Borough",
                "Latitude",
                "Longitude"
        };
        Cursor cursor = db.query(
                DATABASE_TABLE_NAME,            // The table to query
                projection,                           // The columns to return
                null,                           // The columns for the WHERE clause
                null,                           // The values for the WHERE clause
                null,                           // don't group the rows
                null,                           // don't filter by row groups
                "Created_Date DESC",            // The sort order
                "60");                          // The limit
        int count = 0;
        while (cursor.moveToNext()) {
            count++;
            String uid = cursor.getString(0);
            String date = cursor.getString(1);
            String locType = cursor.getString(2);
            String incZip = cursor.getString(3);
            String incAddress = cursor.getString(4);
            String city = cursor.getString(5);
            String borough = cursor.getString(6);
            Float lat = Float.parseFloat(cursor.getString(7));
            Float lng = Float.parseFloat(cursor.getString(8));
            Location loc = new Location(locType, incZip, incAddress, city, borough, lat, lng);
            RatSighting aRatSighting = new RatSighting(uid, date, loc);
            if (!RatSighting.ratSightingHashMap.containsKey(uid)) {
                RatSighting.ratSightingHashMap.put(uid, aRatSighting);
                RatSighting.ratSightingArray.add(aRatSighting);
            }
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        mainAdapter.notifyDataSetChanged();
        return count;
    }

    public int loadListFromDb(String fromDate, String toDate) {
        DataLogger dbHelper = DataLogger.getHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransactionNonExclusive();
        Cursor cursor = db.query(DATABASE_TABLE_NAME, null, "Created_Date BETWEEN Date(?) AND Date(?)", new String[] {fromDate, toDate}, null, null, "Created_Date ASC", null);

        RatSighting.ratSightingHashMap.clear();
        RatSighting.ratSightingArray.clear();
        int count = 0;
        while (cursor.moveToNext()) {
            count++;
            String uid = cursor.getString(0);
            String date = cursor.getString(1);
            String locType = cursor.getString(2);
            String incZip = cursor.getString(3);
            String incAddress = cursor.getString(4);
            String city = cursor.getString(5);
            String borough = cursor.getString(6);
            Float lat = Float.parseFloat(cursor.getString(7));
            Float lng = Float.parseFloat(cursor.getString(8));
            Location loc = new Location(locType, incZip, incAddress, city, borough, lat, lng);
            RatSighting aRatSighting = new RatSighting(uid, date, loc);
            if (!RatSighting.ratSightingHashMap.containsKey(uid)) {
                RatSighting.ratSightingHashMap.put(uid, aRatSighting);
                RatSighting.ratSightingArray.add(aRatSighting);
            }
        }
        cursor.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        mainAdapter.notifyDataSetChanged();
        //mainList.setAdapter(mainAdapter);
        return count;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent cityClick = new Intent(MainActivity.this, SightingListActivity.class);
        cityClick.putExtra("RatSighting", (RatSighting) mainAdapter.getItem(i));
        startActivity(cityClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        if(view == fromDateEditTxt) {
            fromDateDialog.show();
        } else if(view == toDateEditTxt) {
            toDateDialog.show();
        }
    }
}
