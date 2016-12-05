package com.example.perds.morenotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
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
    private String filePath;
    //private MediaPlayer mp;

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
        //loadImageFromStorage(filePath);
        //loadImageFromStorage("/data/data/com.example.perds.morenotes/app_imageDir");
    }
/*
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
    } */

    public void playMusic (View v) {
        MediaPlayer mp = new MediaPlayer();
        //String filePathMusic = Environment.getExternalStorageDirectory().getAbsolutePath()+"app_imageDir/test.mp4";
        String fileName = "test";
        //mediaPlayer.setDataSource(fileInputStream.getFD());

        try {
            //Runtime.getRuntime().exec("chmod 777 /data/data/com.example.perds.morenotes/app_imageDir/test.mp4");
            FileInputStream fileInputStream = new FileInputStream("/data/data/com.example.perds.morenotes/app_imageDir/" + fileName  + ".mp4");
            //mp.setDataSource(fileInputStream.getFD());
            mp.reset();
            mp.setDataSource(fileInputStream.getFD());
            fileInputStream.close();
            //mp.setDataSource(filePathMusic);

            mp.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Exception of type : " + e.toString());
            e.printStackTrace();
        }
        mp.start();
    }
}
