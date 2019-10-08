package com.digitalchannelforum.ui.drawer;


import com.digitalchannelforum.drupalcon.model.UpdateRequest;

import java.util.List;

public interface EventHolderFragmentStrategy {
    List<Long> getDayList();

    int getTextResId();

    int getImageResId();

    boolean updateFavorites();

    boolean update(List<UpdateRequest> requests);

    EventMode getEventMode();

    boolean isMySchedule();
}
