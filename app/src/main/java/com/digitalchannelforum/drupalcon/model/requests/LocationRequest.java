package com.digitalchannelforum.drupalcon.model.requests;

import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.data.Location;
import com.digitalchannelforum.http.base.BaseRequest;

import java.util.Map;

public class LocationRequest extends BaseSafeConsumeContainerRequest<Location.Holder> {

    public LocationRequest(DrupalClient client) {
        super(client, new Location.Holder());
    }

    @Override
    protected String getPath() {
        return "getLocations";
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
