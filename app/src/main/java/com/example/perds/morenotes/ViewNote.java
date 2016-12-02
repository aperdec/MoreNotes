package com.example.perds.morenotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.perds.morenotes.beans.Note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ViewNote extends AppCompatActivity {

    private TextView title;
    private TextView message;
    private TextView loc;
    private TextView date;
    private TextView cat;
    private Note note;
    private Intent intent;
    private static final int EDIT_NOTE = 1;
    private static final int VIEW_PIC = 33;
    private String filePath;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        title = (TextView) findViewById(R.id.txtTitle);
        message = (TextView) findViewById(R.id.txtMessege);
        loc  = (TextView) findViewById(R.id.txtLocation);
        date  = (TextView) findViewById(R.id.txtDate);
        cat  = (TextView) findViewById(R.id.txtCategory);

        intent = getIntent();
        note = intent.getParcelableExtra("note");
        title.setText(note.getTitle());
        message.setText(note.getText());
        loc.setText(note.getLocation());
        date.setText(note.getDateCreated());
        cat.setText(note.getCategory());
        filePath = note.getPicture();

        id=note.getId();

    }

    public void editNote(View v){
        Intent i = new Intent();
        i.setClass(this, NoteEditing.class);
        i.putExtra("editNote", note);
        startActivityForResult(i, EDIT_NOTE);
    }

    public void delNote(View v){
        Intent intent = new Intent();
        intent.putExtra("note", note);
        intent.putExtra("action", "delete");
        setResult(RESULT_OK, intent);
        finish();
    }

    public void viewImg(View v){
        Intent pic = new Intent();
        pic.setClass(this, PictureView.class);
        pic.putExtra("id", id);
        startActivityForResult(pic, VIEW_PIC);
        //loadImageFromStorage(filePath);
        //loadImageFromStorage("/data/data/com.example.perds.morenotes/app_imageDir");
    }

    private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imageView);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
