package com.digitalchannelforum.drupalcon.model.requests;

import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.Model;
import com.digitalchannelforum.drupalcon.model.data.Schedule;
import com.digitalchannelforum.http.base.BaseRequest;

import java.util.Map;

public class ScheduleRequest extends BaseScheduleRequest<Schedule.Holder> {

    public ScheduleRequest(DrupalClient client) {
        super(client, new Schedule.Holder());
    }

    @Override
    protected String getPath() {
        return Model.instance().getSharedScheduleManager().getPath();
    }

    @Override
    protected Map<String, String> getItemRequestPostParameters() {
        return null;
    }

    @Override
    protected Map<String, Object> getItemRequestGetParameters(BaseRequest.RequestMethod method) {
        return null;
    }
}