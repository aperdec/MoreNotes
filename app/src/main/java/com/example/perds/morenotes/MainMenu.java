package com.example.perds.morenotes;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.perds.morenotes.beans.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private static final String NOTE_PREFS = "NotePrefs";
    private static final String SETTINGS_PREFS_NOTES = "SettingsPrefsNotes";

    private ListView lstNotes;

    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        notes = new ArrayList<>();
        notes.add(new Note(1, "First Note", "Bubbles", "This is note number 1", "45.32, 3.232", new Date(), null, null));
        notes.add(new Note(2, "Second Note", "Bath", "OMG this is like totaly the second note", "34.322, 23.232", new Date(), null, null));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                                                startActivity(intent);
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
}
