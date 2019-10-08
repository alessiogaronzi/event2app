package com.digitalchannelforum.drupalcon.model.requests;

import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.data.POI;
import com.digitalchannelforum.http.base.BaseRequest;

import java.util.Map;

public class PoisRequest extends BaseSafeConsumeContainerRequest<POI.Holder> {

    public PoisRequest(DrupalClient client) {
        super(client, new POI.Holder());
    }

    @Override
    protected String getPath() {
        return "getPOI";
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