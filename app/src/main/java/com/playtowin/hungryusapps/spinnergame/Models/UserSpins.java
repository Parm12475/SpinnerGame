package com.playtowin.hungryusapps.spinnergame.Models;


import android.os.Parcel;
import android.os.Parcelable;


public class UserSpins implements Parcelable
{
    private String score;
    private String date;
    private String source;

    public UserSpins(String score)
    {
        this.score = score;
    }

    public UserSpins(String mScore,String source)
    {
        this.score = mScore;
        this.source = source;
    }//UserSpins

    public UserSpins(String score, String date, String source) {
        this.score = score;
        this.date = date;
        this.source = source;
    }

    public UserSpins()
    {
    }//UserSpins


    protected UserSpins(Parcel in) {
        score = in.readString();
        date = in.readString();
        source = in.readString();
    }

    public static final Creator<UserSpins> CREATOR = new Creator<UserSpins>() {
        @Override
        public UserSpins createFromParcel(Parcel in) {
            return new UserSpins(in);
        }

        @Override
        public UserSpins[] newArray(int size) {
            return new UserSpins[size];
        }
    };

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "UserSpins{" +
                "score='" + score + '\'' +
                ", date='" + date + '\'' +
                ", source='" + source + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(score);
        dest.writeString(date);
        dest.writeString(source);
    }
}//UserSpins
