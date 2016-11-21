package com.example.perds.morenotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.perds.morenotes.beans.Note;

public class ViewNote extends AppCompatActivity {

    private TextView title;
    private TextView message;
    private TextView loc;
    private TextView date;
    private TextView cat;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        title = (TextView) findViewById(R.id.txtTitle);
        message = (TextView) findViewById(R.id.txtMessege);
        loc  = (TextView) findViewById(R.id.txtLocation);
        date  = (TextView) findViewById(R.id.txtDate);
        cat  = (TextView) findViewById(R.id.txtCategory);

        Intent intent = getIntent();
        note = intent.getParcelableExtra("note");
        title.setText(note.getTitle());
        message.setText(note.getText());
        loc.setText(note.getText());
        //date.setText(note.getText());
        cat.setText(note.getCategory());

    }
}
