package com.example.perds.morenotes;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.perds.morenotes.beans.Note;
import com.example.perds.morenotes.beans.NotesDB;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainMenu extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String NOTE_PREFS = "NotePrefs", SETTINGS_PREFS_NOTES = "SettingsPrefsNotes";
    private static final int EDIT_NOTE = 1, VIEW_NOTE = 2;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int NEW_NOTE = 3;
    public String locationStr;
    public static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;

    private ListView lstNotes;

    private List<Note> notes;
    private NotesDB notesDB;
    private GoogleMap mMap;
    private boolean mPermissionDenied = false;

    LocationRequest mLocationRequest;

    double lat, lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        locationStr = null;
        notesDB = new NotesDB(this);
        notes = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), NoteEditing.class);
                Note newNote = new Note();
                newNote.setId(getNoteId());
                newNote.setDateCreated(new Date().toString());
                newNote.setLocation(locationStr);
                intent.putExtra("note", newNote);
                intent.putExtra("action", "save");
                startActivityForResult(intent, NEW_NOTE);

                connect();

            }
        });

        lstNotes = (ListView) findViewById(R.id.lstNotes);
        lstNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Log.i("Here", "Replace with intent. Position:" + position + " Id" + id);
                                                Log.i("Sending", "Sending this note: " + notes.get(position).getTitle());
                                                Intent intent = new Intent();
                                                intent.setClass(parent.getContext(), ViewNote.class);
                                                intent.putExtra("note", notes.get(position));
                                                startActivityForResult(intent, VIEW_NOTE);
                                            }
                                        }
        );

        SharedPreferences settings = getSharedPreferences(NOTE_PREFS, MODE_PRIVATE);
        if (settings.contains(SETTINGS_PREFS_NOTES)) {
            MyArrayAdapter myArrayAdapter = new MyArrayAdapter(this, R.layout.fragment_note_in_list, notes);

            lstNotes.setAdapter(myArrayAdapter);
        } else {
            MyArrayAdapter myArrayAdapter = new MyArrayAdapter(this, R.layout.fragment_note_in_list, notes);

            lstNotes.setAdapter(myArrayAdapter);
        }

        // This is to load the database
        notes = notesDB.getAllNotes();
        MyArrayAdapter myArrayAdapter = new MyArrayAdapter(this, R.layout.fragment_note_in_list, notes);
        lstNotes.setAdapter(myArrayAdapter);

        //get address stuff
        // addressTextView = (TextView) findViewById(R.id.txtLocation);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        connect();
    }

    public int getNoteId() {
        int id = 1;
        for (Note note : notes) {
            if (note.getId() >= id) {
                id = note.getId() + 1;
            }
        }
        return id;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Activity Result", "Request Code:" + requestCode + ", Result Code:" + resultCode);

        if (requestCode == VIEW_NOTE || requestCode == EDIT_NOTE || requestCode == NEW_NOTE) {

            if (resultCode == RESULT_OK) {
                String action = data.getStringExtra("action");
                Note note = data.getParcelableExtra("note");
                switch (action) {
                    case "delete":
                        Log.i("Delete", "Note " + note.getId() + " is being removed");
                        notesDB.deleteNotesById(note.getId());
                        break;
                    case "save":
                        Log.i("Save", "Note " + note.getId() + " is being added");
                        notesDB.saveNote(note);
                        break;
                    case "update":
                        Log.i("Update", "Note " + note.getId() + " is being updated");
                        notesDB.updateNoteById(note);
                        break;
                    default:
                        break;
                }
            } else {
                Log.i("Canceled", "The activity has returned with a cancel result code");
            }
        }
        notes = notesDB.getAllNotes();
        MyArrayAdapter myArrayAdapter = new MyArrayAdapter(this, R.layout.fragment_note_in_list, notes);
        lstNotes.setAdapter(myArrayAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        //connect();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            //enable my location here *****************************************
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
        } else {
            // Access to the location has been granted to the app.

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    public void connect() {

        mGoogleApiClient.connect();


        if (mGoogleApiClient.isConnecting() == true) {
            //Toast.makeText(getApplicationContext(), "connecting", Toast.LENGTH_SHORT).show();
        } else if (mGoogleApiClient.isConnected() == true) {
           // Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getApplicationContext(), "not connected", Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        mGoogleApiClient.connect();

        Location location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            locationStr = String.valueOf(lat) + "," + String.valueOf(lng);

            // Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG).show();
        } else {
            locationStr = "122.0,33.0";
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
