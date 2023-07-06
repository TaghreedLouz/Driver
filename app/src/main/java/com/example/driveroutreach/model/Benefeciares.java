
package com.example.driveroutreach.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

public class Benefeciares implements Parcelable {
    String name;
    int mobile;
    String region;
    String address;
    String age;
    String disabilityType;
    String organization;
    String gender;

    GeoPoint location;
    String documentId;
    String ImgUrl;


    public Benefeciares(GeoPoint location) {
        this.location = location;
    }

    public Benefeciares() {
    }

    public Benefeciares(String name, int mobile, String region, String address, String age, String disabilityType, String organization, String gender, GeoPoint location, String documentId, String imgUrl) {
        this.name = name;
        this.mobile = mobile;
        this.region = region;
        this.address = address;
        this.age = age;
        this.disabilityType = disabilityType;
        this.organization = organization;
        this.gender = gender;
        this.location = location;
        this.documentId = documentId;
        ImgUrl = imgUrl;
    }

    public Benefeciares(String name, int mobile, String region, String address, String age, String disabilityType, String organization, String gender, String documentId) {
        this.name = name;
        this.mobile = mobile;
        this.region = region;
        this.address = address;
        this.age = age;
        this.disabilityType = disabilityType;
        this.organization = organization;
        this.gender = gender;
        this.documentId = documentId;
    }

    protected Benefeciares(Parcel in) {
        name = in.readString();
        mobile = in.readInt();
        region = in.readString();
        address = in.readString();
        age = in.readString();
        disabilityType = in.readString();
        organization = in.readString();
        gender = in.readString();
        documentId = in.readString();
    }

    public static final Creator<Benefeciares> CREATOR = new Creator<Benefeciares>() {
        @Override
        public Benefeciares createFromParcel(Parcel in) {
            return new Benefeciares(in);
        }

        @Override
        public Benefeciares[] newArray(int size) {
            return new Benefeciares[size];
        }
    };

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDisabilityType() {
        return disabilityType;
    }

    public void setDisabilityType(String disabilityType) {
        this.disabilityType = disabilityType;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(mobile);
        parcel.writeString(region);
        parcel.writeString(address);
        parcel.writeString(age);
        parcel.writeString(disabilityType);
        parcel.writeString(organization);
        parcel.writeString(gender);
        parcel.writeString(documentId);
    }
}
