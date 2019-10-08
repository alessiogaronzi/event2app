package com.digitalchannelforum.drupalcon.model.requests;

import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.data.Speaker;
import com.digitalchannelforum.http.base.BaseRequest;

import java.util.Map;

public class SpeakersRequest extends BaseSafeConsumeContainerRequest<Speaker.Holder> {

    public SpeakersRequest(DrupalClient client) {
        super(client, new Speaker.Holder());
    }

    @Override
    protected String getPath() {
        return "getSpeakers";
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
