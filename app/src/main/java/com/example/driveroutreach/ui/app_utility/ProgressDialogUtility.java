package com.example.driveroutreach.ui.app_utility;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogUtility {

   static ProgressDialog showProgressBar(Context context,String Message){
       //Setting the progress
       ProgressDialog progressDialog= new ProgressDialog(context);
       progressDialog.setTitle("Uploading...");
       progressDialog.setCancelable(false);
       progressDialog.show();

       return progressDialog;
   }


}
