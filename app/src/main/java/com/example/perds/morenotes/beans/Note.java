package com.example.perds.morenotes.beans;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import java.util.Date;

/**
 * Created by Perds on 11/8/2016.
 */

public class Note implements Parcelable {

    private int id;
    private String title;
    private String category;
    private String text;
    private String location;
    private Date dateCreated;
    private Bitmap picture;
    private MediaStore.Audio.Media audio;

    public Note(int id, String title, String category, String text, String location, Date dateCreated, Bitmap picture, MediaStore.Audio.Media audio) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.text = text;
        this.location = location;
        this.dateCreated = dateCreated;
        this.picture = picture;
        this.audio = audio;
    }

    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        category = in.readString();
        text = in.readString();
        location = in.readString();
        dateCreated = in.readParcelable(Date.class.getClassLoader());
        picture = in.readParcelable(Bitmap.class.getClassLoader());
        audio = in.readParcelable(MediaStore.Audio.Media.class.getClassLoader());
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public MediaStore.Audio.Media getAudio() {
        return audio;
    }

    public void setAudio(MediaStore.Audio.Media audio) {
        this.audio = audio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(text);
        dest.writeString(location);
        dest.writeValue(dateCreated);
        dest.writeValue(picture);
        dest.writeValue(audio);
    }
}
