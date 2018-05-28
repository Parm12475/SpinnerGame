package com.playtowin.hungryusapps.spinnergame.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable
{
    private String user_id;
    private String email;
    private String name ;
    private long score;

    public User(String user_id, String email, String name, long score) {
        this.user_id = user_id;
        this.email = email;
        this.name = name;
        this.score = score;
    }

    public User()
    {
    }

    protected User(Parcel in)
    {
        user_id = in.readString();
        email = in.readString();
        name = in.readString();
        score = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(email);
        dest.writeString(name);
        dest.writeLong(score);
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                '}';
    }
}//User
