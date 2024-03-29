package com.digitalchannelforum.ui.drawer;

import com.digitalchannelforum.drupalcon.R;
import com.digitalchannelforum.drupalcon.model.Model;
import com.digitalchannelforum.drupalcon.model.UpdateRequest;
import java.util.List;

public class FavoritesStrategy implements EventHolderFragmentStrategy {

    @Override
    public List<Long> getDayList() {
        return Model.instance().getSharedScheduleManager().getFavoriteEventDays();
    }

    @Override
    public int getTextResId() {
        return R.string.placeholder_schedule;
    }

    @Override
    public int getImageResId() {
        return R.drawable.ic_no_my_schedule;
    }

    @Override
    public boolean updateFavorites() {
        return true;
    }

    @Override
    public boolean update(List<UpdateRequest> requests) {
        return true;
    }

    @Override
    public EventMode getEventMode() {
        return EventMode.Favorites;
    }

    @Override
    public boolean isMySchedule() {
        return true;
    }
}
