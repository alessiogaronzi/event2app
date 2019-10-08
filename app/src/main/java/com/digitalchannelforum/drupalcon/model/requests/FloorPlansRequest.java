package com.digitalchannelforum.drupalcon.model.requests;

import com.digitalchannelforum.drupal.DrupalClient;
import com.digitalchannelforum.drupalcon.model.data.FloorPlan;
import com.digitalchannelforum.http.base.BaseRequest;

import java.util.Map;

public class FloorPlansRequest extends BaseSafeConsumeContainerRequest<FloorPlan.Holder> {

    public FloorPlansRequest(DrupalClient client) {
        super(client, new FloorPlan.Holder());
    }

    @Override
    protected String getPath() {
        return "getFloorPlans";
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
