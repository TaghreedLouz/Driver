package com.example.driveroutreach.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class DriverProfile implements Parcelable {
    String name;
    int mobile;
    String region;
    int vichuleId;
    String dayOff;
    LatLng coordinates;


    public DriverProfile(String name, int mobile, String region, int vichuleId, String dayOff, LatLng coordinates) {
        this.name = name;
        this.mobile = mobile;
        this.region = region;
        this.vichuleId = vichuleId;
        this.dayOff = dayOff;
        this.coordinates = coordinates;
    }

    public DriverProfile() {
    }

    public DriverProfile(String name, int mobile, String region, int vichuleId, String dayOff) {
        this.name = name;
        this.mobile = mobile;
        this.region = region;
        this.vichuleId = vichuleId;
        this.dayOff = dayOff;
    }

    protected DriverProfile(Parcel in) {
        name = in.readString();
        mobile = in.readInt();
        region = in.readString();
        vichuleId = in.readInt();
        dayOff = in.readString();

    }

    public static final Creator<DriverProfile> CREATOR = new Creator<DriverProfile>() {
        @Override
        public DriverProfile createFromParcel(Parcel in) {
            return new DriverProfile(in);
        }

        @Override
        public DriverProfile[] newArray(int size) {
            return new DriverProfile[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getVichuleId() {
        return vichuleId;
    }

    public void setVichuleId(int vichuleId) {
        this.vichuleId = vichuleId;
    }

    public String getDayOff() {
        return dayOff;
    }

    public void setDayOff(String dayOff) {
        this.dayOff = dayOff;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
              parcel.writeString(this.name);
              parcel.writeInt(this.mobile);
              parcel.writeString(this.region);
              parcel.writeInt(this.vichuleId);
              parcel.writeString(this.dayOff);

    }
}
