package com.digitalchannelforum.drupalcon.model.managers;

import com.digitalchannelforum.drupal.AbstractBaseDrupalEntity;
import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.PreferencesManager;
import com.digitalchannelforum.drupalcon.model.data.Event;
import com.digitalchannelforum.drupalcon.model.requests.BofsRequest;
import com.digitalchannelforum.ui.adapter.item.EventListItem;
import com.digitalchannelforum.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class BofsManager extends EventManager {

    public BofsManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new BofsRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "bofs";
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> bofs = requestResponse.getDays();
        if (bofs == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        for (Event.Day day : bofs) {

            for (Event event : day.getEvents()) {
                if (event != null) {

                    Date date = DateUtils.getInstance().convertEventDayDate(day.getDate());
                    if (date != null) {
                        event.setDate(date);
                    }
                    event.setEventClass(Event.BOFS_CLASS);

                    for (long id : ids) {
                        if (event.getId() == id) {
                            event.setFavorite(true);
                            break;
                        }
                    }

                    mEventDao.saveOrUpdateSafe(event);
                    saveEventSpeakers(event);

                    if (event.isDeleted()) {
                        deleteEvent(event);
                    }

                }
            }
        }
        return true;
    }

//    public List<Long> getBofsDays() {
//        return mEventDao.selectDistrictDateSafe(Event.BOFS_CLASS);
//    }

    public List<Long> getBofsDays() {
        List<Long> levelIds = PreferencesManager.getInstance().loadExpLevel();
        List<Long> trackIds = PreferencesManager.getInstance().loadTracks();

        if (levelIds.isEmpty() & trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateSafe(Event.BOFS_CLASS);

        } else if (!levelIds.isEmpty() & !trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByTrackAndLevelIdsSafe(Event.BOFS_CLASS, levelIds, trackIds);

        } else if (!levelIds.isEmpty() & trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByLevelIdsSafe(Event.BOFS_CLASS, levelIds);

        } else {
            return mEventDao.selectDistrictDateByTrackIdsSafe(Event.BOFS_CLASS, trackIds);
        }
    }

    public List<EventListItem> getBofsItemsSafe(long day) {
        return mEventDao.selectBofsItemsSafe(Event.BOFS_CLASS, day);
    }
}
