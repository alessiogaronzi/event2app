package com.digitalchannelforum.drupalcon.model.managers;

import com.digitalchannelforum.drupal.AbstractBaseDrupalEntity;
import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.PreferencesManager;
import com.digitalchannelforum.drupalcon.model.data.Event;
import com.digitalchannelforum.drupalcon.model.requests.SessionsRequest;
import com.digitalchannelforum.ui.adapter.item.EventListItem;
import com.digitalchannelforum.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class ProgramManager extends EventManager {

    public ProgramManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new SessionsRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "sessions";
    }

    @Override
    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> sessions = requestResponse.getDays();
        if (sessions == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        for (Event.Day day : sessions) {

            for (Event event : day.getEvents()) {
                if (event != null) {

                    Date date = DateUtils.getInstance().convertEventDayDate(day.getDate());
                    if (date != null) {
                        event.setDate(date);
                    }
                    event.setEventClass(Event.PROGRAM_CLASS);

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

    public List<Long> getProgramDays() {
        List<Long> levelIds = PreferencesManager.getInstance().loadExpLevel();
        List<Long> trackIds = PreferencesManager.getInstance().loadTracks();

        if (levelIds.isEmpty() & trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateSafe(Event.PROGRAM_CLASS);

        } else if (!levelIds.isEmpty() & !trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByTrackAndLevelIdsSafe(Event.PROGRAM_CLASS, levelIds, trackIds);

        } else if (!levelIds.isEmpty() & trackIds.isEmpty()) {
            return mEventDao.selectDistrictDateByLevelIdsSafe(Event.PROGRAM_CLASS, levelIds);

        } else {
            return mEventDao.selectDistrictDateByTrackIdsSafe(Event.PROGRAM_CLASS, trackIds);
        }
    }

    public List<EventListItem> getProgramItemsSafe(int eventClass, long day, List<Long> levelIds, List<Long> trackIds) {
        return mEventDao.selectProgramItemsSafe(eventClass, day, levelIds, trackIds);
    }

    public List<EventListItem> getFavoriteProgramItemsSafe(List<Long> favoriteEventIds, long day){
        return mEventDao.selectFavoriteProgramItemsSafe(favoriteEventIds, day);
    }
}