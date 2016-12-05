package com.example.perds.morenotes;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.perds.morenotes.beans.Note;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.vision.text.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NoteEditing extends AppCompatActivity {

    private final static int CONNECTION_FAILURE_REQUEST = 9000;
    private TextView addressTextView;
    private static final String CAM_PREF = "";
    static final int TAKE_AVATAR_CAMERA_REQUEST = 10;
    static final int CAPTURE_IMAGE_ACTIVITY = 5;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 30;
    private static final String SETTINGS_PREFS_AVATAR = "";

    private int id;
    private EditText edtTitle;
    private Spinner edtCategory;
    private EditText edtText;
    private String date;
    private String location;
    private String picture;
    private String audio;
    private String action;
    private Note note;
    private TextView errorMessage;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editing);

        edtTitle = (EditText) findViewById(R.id.editText);
        edtCategory = (Spinner) findViewById(R.id.spinner2);
        edtText = (EditText) findViewById(R.id.txtMessege);
        errorMessage = (TextView) findViewById(R.id.errorText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        edtCategory.setAdapter(adapter);

        Intent intent = getIntent();
        note = intent.getParcelableExtra("note");
        action = intent.getStringExtra("action");

        id = note.getId();
        location = note.getLocation();
        date = note.getDateCreated();
        //picture = note.getPicture();
        picture = "pic"+note.getId()+".png";
        audio = note.getAudio();

        edtTitle.setText(note.getTitle());
        edtCategory.setSelection(matchSelected(note.getCategory()));
        edtText.setText(note.getText());
    }

    private int matchSelected(String category) {
        String[] categories = this.getResources().getStringArray(R.array.planets_array);
        if (category != null) {
            for (int i = 0; i < categories.length; i++) {
                if (category.equals(categories[i])) {
                    return i;
                }
            }
        }
        return 0;
    }

    //camera code
    public void onClickCam(View v) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.CAMERA, true);
        } else {
            // Access to the location has been granted to the app.
            /*
            String path = Environment.getExternalStorageDirectory() + "/photo1.jpg";
            File file = new File(path);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);*/
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY);
        }

    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        //File mypath = new File(directory, note.getId() + ".jpg");
        File mypath = new File(directory, picture);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    //takes the response from the camera -- if working the result will have an img
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_AVATAR_CAMERA_REQUEST:
                if (resultCode == Activity.RESULT_CANCELED) {
                    //app stopped
                } else if (resultCode == Activity.RESULT_OK) {
                    //camera worked
                }
        }

        Bitmap camPic = (Bitmap) data.getExtras().get("data");
        if (camPic != null) {
            try {
                System.out.print("camera working");
                //picture = saveToInternalStorage(camPic);
                saveToInternalStorage(camPic);
                Log.i("file saved", picture + " it worked");
            } catch (Exception e) {
                //save didn't work
                Log.i("fail", "failure");
            }
        }
    }

    //audio coding
    public void play(View v) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.RECORD_AUDIO, true);
        } else {
            //recordAudio(id);
            recordAudio("test");
        }

    }

    public void recordAudio(String fileName) {
        final MediaRecorder recorder = new MediaRecorder();
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.MediaColumns.TITLE, fileName);
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile("/data/data/com.example.perds.morenotes/app_imageDir/" + fileName  + ".mp4");
        try {
            recorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final ProgressDialog mProgressDialog = new ProgressDialog(NoteEditing.this);
        //mProgressDialog.setTitle(R.string.lbl_recording);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setButton("Stop recording", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mProgressDialog.dismiss();
                recorder.stop();
                recorder.release();
            }
        });

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface p1) {
                recorder.stop();
                recorder.release();
            }
        });
        recorder.start();
        mProgressDialog.show();

    }

    public void saveNote(View view) {
        boolean error = false;
        Intent intent = new Intent();
        Note note = new Note();
        note.setId(id);
        note.setLocation(location);
        note.setDateCreated(date);
        note.setPicture(picture);
        note.setAudio(audio);
        note.setTitle(edtTitle.getText().toString());
        note.setCategory(edtCategory.getSelectedItem().toString());
        note.setText(edtText.getText().toString());

        intent.putExtra("note", note);
        intent.putExtra("action", action);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void displayMap(String location) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("location", location);

    }

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
