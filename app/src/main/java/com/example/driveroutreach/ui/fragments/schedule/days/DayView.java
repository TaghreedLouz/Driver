package com.example.driveroutreach.ui.fragments.schedule.days;

import com.example.driveroutreach.model.JourneyModel;

import java.util.ArrayList;

public interface DayView {

    void  onGettingScheduleSuccess(ArrayList<JourneyModel> Trips);
    void  onGettingScheduleFailure(Exception e);


    void storeArchivedJourneySuccess();
    void storeArchivedJourneyFailure(Exception e);


    void storeArchivedJourneySuccess2();
    void storeArchivedJourneyFailure2(Exception e);

}
