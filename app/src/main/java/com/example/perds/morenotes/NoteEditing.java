package com.example.perds.morenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class NoteEditing extends AppCompatActivity {

    private final static int CONNECTION_FAILURE_REQUEST = 9000;
    private TextView addressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editing);

        // text box
        // location
        // date
        // picture
        // audio

        Intent intent = getIntent();
        double latlng = intent.getDoubleExtra("latitude", 0.0);

        addressTextView.setText(Double.toString(latlng));

    }

    public void getLocation(){
        //get location

    }

    public void getDate(){
        // get date

    }



}
