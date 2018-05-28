package com.playtowin.hungryusapps.spinnergame.Models;


import android.os.Parcel;
import android.os.Parcelable;

public class AccountDetail implements Parcelable
{
    String account_detail;

    public AccountDetail(String account_detail)
    {
        this.account_detail = account_detail;
    }

    protected AccountDetail(Parcel in) {
        account_detail = in.readString();
    }

    public static final Creator<AccountDetail> CREATOR = new Creator<AccountDetail>() {
        @Override
        public AccountDetail createFromParcel(Parcel in) {
            return new AccountDetail(in);
        }

        @Override
        public AccountDetail[] newArray(int size) {
            return new AccountDetail[size];
        }
    };

    public String getAccount_detail() {
        return account_detail;
    }

    public void setAccount_detail(String account_detail) {
        this.account_detail = account_detail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account_detail);
    }
}//AccountDetail
