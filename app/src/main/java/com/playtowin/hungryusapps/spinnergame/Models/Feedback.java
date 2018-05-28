package com.playtowin.hungryusapps.spinnergame.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class Feedback implements Parcelable
{
    private String Feedback;
    private String date;

    public Feedback(String feedback, String date)
    {
        Feedback = feedback;
        this.date = date;
    }

    public Feedback() {
    }

    protected Feedback(Parcel in) {
        Feedback = in.readString();
        date = in.readString();
    }

    public static final Creator<Feedback> CREATOR = new Creator<Feedback>() {
        @Override
        public Feedback createFromParcel(Parcel in) {
            return new Feedback(in);
        }

        @Override
        public Feedback[] newArray(int size) {
            return new Feedback[size];
        }
    };

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Feedback);
        dest.writeString(date);
    }
}
