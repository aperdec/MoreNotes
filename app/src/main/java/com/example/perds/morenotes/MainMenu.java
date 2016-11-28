package com.example.perds.morenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.Manifest;

import com.example.perds.morenotes.beans.Note;
import com.example.perds.morenotes.beans.NotesDB;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String NOTE_PREFS = "NotePrefs", SETTINGS_PREFS_NOTES = "SettingsPrefsNotes";
    private static final int EDIT_NOTE = 1, VIEW_NOTE = 2;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleApiClient googleApiClient;

    private ListView lstNotes;

    private List<Note> notes;
    private NotesDB notesDB;
    private GoogleMap mMap;
    private boolean mPermissionDenied = false;


    double lat, lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        notesDB = new NotesDB(this);
        notes = new ArrayList<>();
        notes.add(new Note(1, "First Note", "Bubbles", "This is note number 1", "45.32, 3.232", "11/11/11", null, null));
        notes.add(new Note(2, "Second Note", "Bath", "OMG this is like totaly the second note", "34.322, 23.232", "12/12/12", null, null));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(MainMenu.this, NoteEditing.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

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
//        notes = notesDB.getAllNotes();
//        MyArrayAdapter myArrayAdapter = new MyArrayAdapter(this, R.layout.fragment_note_in_list, notes);
//        lstNotes.setAdapter(myArrayAdapter);

        //get address stuff
       // addressTextView = (TextView) findViewById(R.id.txtLocation);
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
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

        if (requestCode == VIEW_NOTE) {

            if (resultCode == RESULT_OK) {
                String action = data.getStringExtra("action");
                Note note = data.getParcelableExtra("note");
                switch (action) {
                    case "delete" :
                        Log.i("Delete", "Note " + note.getId() + " is being removed");
                        notesDB.deleteNotesById(note.getId());
                        break;
                    case "save" :
                        Log.i("Save", "Note " + note.getId() + " is being added");
                        notesDB.saveNote(note);
                        break;
                    case "update" :
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
        }


        //startActivity(intent);
     //  Toast toast = Toast.makeText(getApplicationContext(),"test", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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

    public void connect(){
        googleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Intent intent = new Intent(MainMenu.this, NoteEditing.class);
        Location location = LocationServices.FusedLocationApi
                .getLastLocation(googleApiClient);

        if (location != null){
            lat = location.getLatitude();
            lng = location.getLongitude();
            intent.putExtra("latitude", lat);
            intent.putExtra("longitude", lng);
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
