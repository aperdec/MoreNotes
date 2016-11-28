package com.example.perds.morenotes;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class NoteEditing extends AppCompatActivity {

    private final static int CONNECTION_FAILURE_REQUEST = 9000;
    private TextView addressTextView;
    private static final String CAM_PREF = "";
    static final int TAKE_AVATAR_CAMERA_REQUEST = 1;
    static final int CAPTURE_IMAGE_ACTIVITY = 5;
    private static final String SETTINGS_PREFS_AVATAR = "";

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

        //addressTextView.setText(Double.toString(latlng));

    }

    public void getLocation(){
        //get location

    }

    public void getDate(){
        // get date

    }

    //camera code
    public void onClickCam(View v) {
        String path = Environment.getExternalStorageDirectory() + "/photo1.jpg";
        File file = new File(path);
        Uri outputFileUri = Uri.fromFile(file);
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY);
/*
        //Intent pic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Intent camera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uriSavedImage=Uri.fromFile(new File("/sdcard/flashCropped.png"));
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

        startActivityForResult(Intent.createChooser(camera, "Take your photo!"), TAKE_AVATAR_CAMERA_REQUEST);
    */
    }

    //takes the response from the camera -- if working the result will have an img
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.gc();
        if (requestCode == CAPTURE_IMAGE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                /*try {
                    // Call function MakeFolder to create folder structure if
                    // its not created
                    if(imageBitmap != null) {
                        imageBitmap = null;
                        imageBitmap.recycle();
                    }
                    MakeFolder();
                    // Get file from temp1 file where image has been stored
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 3;
                    imageBitmap = BitmapFactory.decodeFile(path, options);
                    imgForPhotograph.setImageBitmap(imageBitmap);
                    isImageTaken = true;
                    // Name for image
                    IMAGEPATH = getString(R.string.chassisImage)
                            + System.currentTimeMillis();
                    SaveImageFile(imageBitmap,IMAGEPATH);
                } catch (Exception e) {
                    Toast.makeText(this, "Picture Not taken",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } */
            }
        }
    }

    /*
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case TAKE_AVATAR_CAMERA_REQUEST:
                if(resultCode == Activity.RESULT_CANCELED){
                    //app stopped
                }
                else if (resultCode == Activity.RESULT_OK){
                    //camera worked
                }
        }

        Bitmap camPic = (Bitmap) data.getExtras().get("data");
        if (camPic != null){
            try{
                System.out.print("camera working");
                //saveAva(camPic);
            } catch (Exception e){
                //save didn't work
            }
        }
    } */

/*
    private void saveAva(Bitmap ava){
        //this is a temp file name
        String fileName = "avatar.png";
        try{
            ava.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(fileName, MODE_PRIVATE));
        }
        catch (Exception e){

        }
        //get img uri to convert to human img from THIS activity/prog
        Uri avaUri = Uri.fromFile(new File(this.getFilesDir(), fileName));
        ImageButton avaBtn = (ImageButton) findViewById(R.id.imgBtn);
        //2 imgs set to refresh
        avaBtn.setImageURI(null);
        avaBtn.setImageURI(avaUri);

        //put img in shared prferences so all activities in prog can access and it will last even if phone restarted
        SharedPreferences settings = getSharedPreferences(CAM_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SETTINGS_PREFS_AVATAR, avaUri.getPath());
        editor.commit();

    }
    */
/*
    public Uri setImageUri() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory,System.currentTimeMillis() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }
*/
}
