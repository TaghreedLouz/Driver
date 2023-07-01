package com.example.driveroutreach.ui.activities.edit_profile;

public interface EditProfileView {


   void onGettingImgeSuccess(String img);
   void onGettingImgFailure(Exception e);

   void onSendCode(String verificationId, String newNumber);


}
