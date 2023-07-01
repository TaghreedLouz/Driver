package com.example.driveroutreach.ui.fragments.archive;

import com.example.driveroutreach.model.ArichivedJourney;

import java.util.ArrayList;

public interface ArichiveView {

void onGettingArchivedJourneysSuccess(ArrayList<ArichivedJourney> journeys);
void onGettingArchivedJourneysFailure(Exception e);

}
