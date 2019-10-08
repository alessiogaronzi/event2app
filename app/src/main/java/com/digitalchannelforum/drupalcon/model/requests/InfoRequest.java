package com.digitalchannelforum.drupalcon.model.requests;

import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.data.InfoItem;
import com.digitalchannelforum.http.base.BaseRequest;

import java.util.Map;

public class InfoRequest extends BaseSafeConsumeContainerRequest<InfoItem.General> {

    public InfoRequest(DrupalClient client) {
        super(client, new InfoItem.General());
    }

    @Override
    protected String getPath() {
        return "getInfo";
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