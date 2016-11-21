package com.example.perds.morenotes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perds.morenotes.beans.Note;

import java.util.List;

/**
 * Created by Perds on 2/24/2016.
 */
public class MyArrayAdapter extends ArrayAdapter<Note> {

    private List<Note> notes;
    private int resourceId;
    private Context context;

    public MyArrayAdapter(Context context, int resourceId, List<Note> notes) {
        super(context, resourceId, notes);
        this.context = context;
        this.resourceId = resourceId;
        this.notes = notes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Inflate convertView if it is null
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(resourceId, parent, false);
        }

        ImageView imgNote = (ImageView) convertView.findViewById(R.id.imgNote);

        int noteResourceId = notes.get(position).getId();

//        imgNote.setImageResource(noteResourceId);

        TextView txtnote = (TextView) convertView.findViewById(R.id.txtTitle);
        // set the text
        txtnote.setText(notes.get(position).getTitle());

        TextView txtCapital = (TextView) convertView.findViewById(R.id.txtCategory);
        // set the text
        txtCapital.setText("Category: " + notes.get(position).getCategory());


        return convertView;
    }
}
