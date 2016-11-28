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
    private String dateCreated;
    private String picture;
    private String audio;

    public Note(int id, String title, String category, String text, String location, String dateCreated, String picture, String audio) {
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
        dateCreated = in.readString();
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

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
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
        dest.writeString(dateCreated);
        dest.writeValue(picture);
        dest.writeValue(audio);
    }
}
