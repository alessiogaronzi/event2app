package com.digitalchannelforum.drupalcon.model.managers;

import com.digitalchannelforum.drupal.AbstractBaseDrupalEntity;
import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.Model;
import com.digitalchannelforum.drupalcon.model.data.SharedEvents;
import com.digitalchannelforum.drupalcon.model.data.Schedule;
import com.digitalchannelforum.drupalcon.model.requests.ScheduleRequest;

import java.util.ArrayList;
import java.util.List;

public class ScheduleManager extends SynchronousItemManager<Schedule.Holder, Object, String> {

    public ScheduleManager(DrupalClient client) {
        super(client);
    }

    @Override
    protected AbstractBaseDrupalEntity getEntityToFetch(DrupalClient client, Object requestParams) {
        return new ScheduleRequest(client);
    }

    @Override
    protected String getEntityRequestTag(Object params) {
        return "scheduleGetEntityRequestTag";
    }

    @Override
    protected boolean storeResponse(Schedule.Holder requestResponse, String tag) {
        ArrayList<SharedEvents> sharedSchedules = new ArrayList<>();
        List<Schedule> schedules = requestResponse.getSchedules();
        for (Schedule schedule : schedules) {
            for (Long eventId : schedule.getEvents()) {
                sharedSchedules.add(new SharedEvents(eventId, schedule.getCode()));
            }
        }
        Model.instance().getSharedScheduleManager().saveSharedEvents(sharedSchedules);
        return true;
    }

}
