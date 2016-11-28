package com.example.perds.morenotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perds.morenotes.beans.Note;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.duration;
import static com.example.perds.morenotes.R.id.text;

public class MainMenu extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String NOTE_PREFS = "NotePrefs";
    private static final String SETTINGS_PREFS_NOTES = "SettingsPrefsNotes";


    private GoogleApiClient googleApiClient;


    private ListView lstNotes;

    private List<Note> notes;

    double lat, lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        lstNotes = (ListView) findViewById(R.id.lstNotes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = new Intent(MainMenu.this, NoteEditing.class);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                onStart();

            }
        });

        SharedPreferences settings = getSharedPreferences(NOTE_PREFS, MODE_PRIVATE);
        if (settings.contains(SETTINGS_PREFS_NOTES)) {


            MyArrayAdapter myArrayAdapter = new MyArrayAdapter(this, R.layout.fragment_note_in_list, notes);

            lstNotes.setAdapter(myArrayAdapter);
        } else {

        }
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

    public void testing() {
        //get address

        //pass with intent

        // MainMenu.this.startActivity(intent);

        onStart();


    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();

     //  Toast toast = Toast.makeText(getApplicationContext(),"test", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
