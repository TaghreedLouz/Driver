package com.example.driveroutreach.ui.activities.Login;

import com.example.driveroutreach.model.DriversNumbers;

public interface LoginView {

void onGetMobileNumber();
void onDriverFound(DriversNumbers num);
    void onFail(Exception exception);
}
