package com.example.driveroutreach.ui.activities.Login;

import com.example.driveroutreach.model.DriversNumbers;

public interface LoginView {
    void onFail(Exception exception);
    void isDriver(DriversNumbers num);
    void numberNotFound();
}
