package com.example.perds.morenotes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PictureView extends AppCompatActivity {

    private Intent pic;
    private ImageView img;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        pic = getIntent();

        id = pic.getIntExtra("id", 0);

        //img=(ImageView) findViewById(R.id.imageView2);
        loadImageFromStorage("/data/data/com.example.perds.morenotes/app_imageDir");
    }

    public void closePic(){
        finish();
    }

    private void loadImageFromStorage(String path)
    {
        try {
            File f=new File(path, id+".jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            img =(ImageView)findViewById(R.id.imageView2);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
