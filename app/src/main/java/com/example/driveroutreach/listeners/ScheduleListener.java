package com.example.driveroutreach.listeners;

import com.example.driveroutreach.model.ArichivedJourney;

public interface ScheduleListener {

  void StartJourney(String journeyId,String date);
  void EndJourney(ArichivedJourney journey);
}
